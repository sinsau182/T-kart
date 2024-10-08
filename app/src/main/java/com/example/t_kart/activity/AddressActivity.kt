package com.example.t_kart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.t_kart.R
import com.example.t_kart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var totalCost: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = this.getSharedPreferences("users", MODE_PRIVATE)

        totalCost = intent.getStringExtra("totalCost")!!

        loadUserInfo()

        binding.proceed.setOnClickListener{
            validateData(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.userVillage.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString(),
                binding.userPinCode.text.toString()
            )
        }
    }

    private fun validateData(name: String, number: String, village: String,
                             city: String, state: String, pinCode: String) {
        if (name.isEmpty() || number.isEmpty() || village.isEmpty() || city.isEmpty() || state.isEmpty() || pinCode.isEmpty()){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
        }
        else{
            storeData(village, city, state, pinCode)
        }
    }

    private fun storeData(village: String, city: String, state: String, pinCode: String) {
        val map = hashMapOf<String, Any>()
        map["village"] = village
        map["state"] = state
        map["city"] = city
        map["pinCode"] = pinCode

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .update(map).addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)
                val intent = Intent(this,  CheckoutActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userNumber"))
                binding.userVillage.setText(it.getString("village"))
                binding.userCity.setText(it.getString("city"))
                binding.userState.setText(it.getString("state"))
                binding.userPinCode.setText(it.getString("pinCode"))
            }
            .addOnFailureListener{

            }
    }
}