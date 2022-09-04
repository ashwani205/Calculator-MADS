package com.example.calculator.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.model.History
import com.example.calculator.utils.MyPreference
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var text: StringBuilder = StringBuilder()
    private var history: ArrayList<History> = ArrayList()
    private var saveAtIndex = 0
    private lateinit var databaseReference: DatabaseReference
    private var isNowLoggedIn = false
    private var firebaseHistoryListSize=0
    private var updateFirebaseListNo=0
    private var isAppOpened =true
    private var userName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setNavigationDrawer()
        mBinding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.first_item -> {
                        drawerLayout.closeDrawers()
                        openHistoryFragment(history)
                    }
                }
                true
            }
        }

        mBinding.signOutBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Do you really want to sign out")
            builder.setPositiveButton("yes") { _, _ ->
                mBinding.progressBar.visibility = View.VISIBLE
                if (MyPreference.readPrefString(this, "loginFrom").equals("google")) {
                    MyPreference.clear(this)
                    FirebaseAuth.getInstance().signOut()
                } else {
                    MyPreference.clear(this)
                }
                startActivity(Intent(this, LoginActivity::class.java))
            }
            builder.setNegativeButton("N0") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        userName = MyPreference.readPrefString(this, "userName") ?: ""
        isNowLoggedIn = intent.getBooleanExtra("isNowLoggedIn", false)
        databaseReference = FirebaseDatabase.getInstance().getReference("history")
            databaseReference.child(userName).addValueEventListener(object : ValueEventListener {
                var historyList = ArrayList<History>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { dataSnapshot ->
                        val message = dataSnapshot.getValue(History::class.java)
                        message?.let { historyList.add(it) }
                    }
                    if(isAppOpened) {
                        firebaseHistoryListSize = historyList.size
                        getLatestHistoryFromDB(historyList)
                        isAppOpened = false
                    }
                    if (isNowLoggedIn) {
                        if(historyList.size>0) {
                            openHistoryFragment(historyList)
                        }
                        isNowLoggedIn = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        mBinding.b1.setOnClickListener {
            setPrimaryText("1")
        }
        mBinding.b2.setOnClickListener {
            setPrimaryText("2")
        }
        mBinding.b3.setOnClickListener {
            setPrimaryText("3")
        }
        mBinding.b4.setOnClickListener {
            setPrimaryText("4")
        }
        mBinding.b5.setOnClickListener {
            setPrimaryText("5")
        }
        mBinding.b6.setOnClickListener {
            setPrimaryText("6")
        }
        mBinding.b7.setOnClickListener {
            setPrimaryText("7")
        }
        mBinding.b8.setOnClickListener {
            setPrimaryText("8")
        }
        mBinding.b9.setOnClickListener {
            setPrimaryText("9")
        }
        mBinding.b0.setOnClickListener {
            setPrimaryText("0")
        }
        mBinding.b00.setOnClickListener {
            setPrimaryText("00")
        }
        mBinding.bClear.setOnClickListener {
            text.clear()
            mBinding.idTVPrimary.text = ""
            mBinding.idTVSecondary.text=""
        }
        mBinding.bHistory.setOnClickListener {
            openHistoryFragment(history)
        }
        mBinding.bBack.setOnClickListener {
            val value = mBinding.idTVPrimary.text.toString()
            if (text.isNotEmpty()) {
                mBinding.idTVPrimary.text = value.substring(0, value.length - 1)
                text.deleteCharAt(value.length - 1)
            }
        }

        mBinding.bMul.setOnClickListener {
            addOperator('*')
        }
        mBinding.bPlus.setOnClickListener {
            addOperator('+')
        }
        mBinding.bDiv.setOnClickListener {
            addOperator('/')
        }
        mBinding.bMinus.setOnClickListener {
            addOperator('-')
        }
        mBinding.bEqual.setOnClickListener {
            if (text.isNotEmpty()) {
                val lastChar = text[text.length - 1]
                if (lastChar != '*' && lastChar != '+' && lastChar != '/' && lastChar != '-') {
                    mBinding.idTVSecondary.text = getResult()
                }
            }
        }
    }


    private fun getResult(): String {
        val operands = ArrayList<String>()
        var operators = ""
        var value = ""
        var count = 0
        if (text[0] == '-') {
            value = "-"
            text = text.deleteCharAt(0)
        }
        text.forEach {
            if (it in '0'..'9' || it == '.') {
                value += it
            } else {
                operands.add(value)
                value = ""
                operators += it
                count++
            }
        }
        operands.add(value)
        value = ""

        while (operators.isNotEmpty()) {
            if (operators.contains("*")) {
                val indexOfOperator = operators.indexOf("*")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(operands, indexOfOperator, '*')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            } else if (operators.contains("+")) {
                val indexOfOperator = operators.indexOf("+")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(operands, indexOfOperator, '+')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            } else if (operators.contains("/")) {
                val indexOfOperator = operators.indexOf("/")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(operands, indexOfOperator, '/')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            } else if (operators.contains("-")) {
                val indexOfOperator = operators.indexOf("-")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(operands, indexOfOperator, '-')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            }
        }
        val historyData =
            History(input = mBinding.idTVPrimary.text.toString(), result = operands[0], time = getTime())
        if(firebaseHistoryListSize<10) {
            addDataToFirebase(historyData)
            firebaseHistoryListSize++
        }else{
            // code for replacing data at index
            if(updateFirebaseListNo>=10)
                updateFirebaseListNo=0
            replaceDataToFirebase(historyData)
            updateFirebaseListNo++
        }

        if (history.size < 10)
            history.add(historyData)
        else {
            history[saveAtIndex] = historyData
            saveAtIndex++
            if (saveAtIndex >= 10)
                saveAtIndex = 0
        }
        return operands[0]
    }

    private fun getTime(): Long{
       return Calendar.getInstance().timeInMillis
    }
    private fun addDataToFirebase(historyData: History) {
//        databaseReference.child(userName).push().setValue(historyData)
        databaseReference.child(userName).child(firebaseHistoryListSize.toString()).setValue(historyData)
    }

    private fun replaceDataToFirebase(historyData: History) {
        databaseReference.child(userName).child(updateFirebaseListNo.toString()).setValue(historyData) //updateChildren(mutableMapOf("0",historyData))
    }

    private fun addOperator(operator: Char) {
        if (text.isNotEmpty()) {
            val lastChar = text[text.length - 1]
            if (lastChar != operator && lastChar !in '0'..'9') {
                text.setCharAt(text.length - 1, operator)
                mBinding.idTVPrimary.text = text
            } else if (lastChar != operator) {
                setPrimaryText("$operator")
            }
        }
    }

    private fun setPrimaryText(action: String) {
        text = text.append(action)
        mBinding.idTVPrimary.text = text
    }

    private fun reSetOperandArray(array: ArrayList<String>, index: Int, operationResult: String) {
        array[index] = operationResult
        if (array.size > index + 1) {
            for (i in index + 1 until array.size - 1) {
                array[i] = array[i + 1]
            }
            array[array.size - 1] = "0"
        }
    }

    private fun operation(operands: ArrayList<String>, index: Int, operation: Char): String {
        val firstOperand = operands[index].toDouble()
        val secondOperand = operands[index + 1].toDouble()
        var result = 0.0
        when (operation) {
            '*' -> {
                result = firstOperand * secondOperand
            }
            '+' -> {
                result = firstOperand + secondOperand
            }
            '/' -> {
                if (secondOperand == 0.0) {
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.divide_by_zero),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else
                    result = firstOperand / secondOperand
            }
            '-' -> {
                result = firstOperand - secondOperand
            }
        }
        return if (result % 1 == 0.0) {
            text.clear()
            text.append(result.toInt())
            result.toInt().toString()
        } else {
            text.clear()
            text.append(result)
            result.toString()
        }
    }

    private fun openHistoryFragment(historyList: ArrayList<History>) {
        val fragment = HistoryFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("history", historyList)
        fragment.arguments = bundle
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun getLatestHistoryFromDB(historyList: ArrayList<History>){
        val maxTime = historyList[0].time
        historyList.forEachIndexed { index, history ->
            if (maxTime != null) {
                if(maxTime < (history.time ?: 0)){
                    updateFirebaseListNo = index
                }
            }
        }
        updateFirebaseListNo++
    }
    @SuppressLint("SetTextI18n")
    private fun setNavigationDrawer() {
        mBinding.navView.getHeaderView(0).findViewById<AppCompatTextView>(R.id.header_title).text =
            "Welcome\n${userName}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) true
        return super.onOptionsItemSelected(item)
    }
}