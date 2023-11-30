package com.example.test

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.ActivityMainBinding
import com.example.test.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<DatabaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        viewModel.load()
        viewModel.getGridLayout()
        Log.e("Viewmodel",viewModel.hashCode().toString())

        binding.fab.setOnClickListener { view ->
            showInputDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.clear_all_data_from_room -> {
                viewModel.clearDataFromRoom()
                Toast.makeText(this, "All data deleted from room", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.reload -> {
                viewModel.load()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_matavimai_dialog_box, null)
        val editText = dialogView.findViewById<EditText>(R.id.etText)
        val integerInput1 = dialogView.findViewById<EditText>(R.id.etInteger1)
        val integerInput2 = dialogView.findViewById<EditText>(R.id.etInteger2)
        val integerInput3 = dialogView.findViewById<EditText>(R.id.etInteger3)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Submit") { dialog, _ ->
                val mac = editText.text.toString()
                val s1 = integerInput1.text.toString().toIntOrNull()
                val s2 = integerInput2.text.toString().toIntOrNull()
                val s3 = integerInput3.text.toString().toIntOrNull()
                if (mac.isEmpty() || s1 == null || s2 == null || s3 == null) {
                    Toast.makeText(this, "Please fill all required texts", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addVartotojaiMACManually(mac, s1, s2, s3)
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}