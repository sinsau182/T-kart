package com.example.t_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.t_kart.MainActivity
import com.example.t_kart.R
import com.example.t_kart.databinding.ActivityProductDetailsBinding
import com.example.t_kart.databinding.ItemCategoryProductLayoutBinding
import com.example.t_kart.roomdb.AppDatabase
import com.example.t_kart.roomdb.ProductDao
import com.example.t_kart.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductDetails(intent.getStringExtra("id"))
    }

    private fun getProductDetails(proId: String?) {
        Firebase.firestore.collection("products")
            .document(proId!!).get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDescr = it.getString("productDescription")
                binding.textViewProdName.text = name
                binding.textViewProdSp.text = productSp
                binding.textViewProdDesc.text = productDescr

                val slideList = ArrayList<SlideModel>()
                for(data in list){
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }

                cartAction(proId, name, productSp, it.getString("productCoverImg"))

                binding.imageSlider.setImageList(slideList)

            }
            .addOnFailureListener{
                Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_SHORT).show()

            }
    }



    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()
        if (productDao.isExit(proId) != null) {
            binding.textViewAddToCart.text = "Go to Cart"
        } else {
            binding.textViewAddToCart.text = "Add to Cart"
        }

        binding.textViewAddToCart.setOnClickListener {
            if (productDao.isExit(proId) != null) {
                openCart()
            } else {
                addToCart(productDao, proId, name, productSp, coverImg)
            }

        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun addToCart(
        productDao: ProductDao,
        proId: String,
        name: String?,
        productSp: String?,
        coverImg: String?)
    {
        val data = ProductModel(proId,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textViewAddToCart.text = "Go to Cart"
        }
    }

}