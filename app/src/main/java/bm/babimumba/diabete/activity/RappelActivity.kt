package bm.babimumba.diabete.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityRappelBinding
import bm.babimumba.diabete.adapter.RappelAdapter
import bm.babimumba.diabete.model.Rappel
import bm.babimumba.diabete.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import java.util.Calendar

class RappelActivity : AppCompatActivity() {
    lateinit var binding: ActivityRappelBinding
    private val rappels = mutableListOf<Rappel>()
    private lateinit var adapter: RappelAdapter
    private val userRepository = UserRepository()
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser?.uid ?: "" }
    @SuppressLint("ScheduleExactAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRappelBinding.inflate(layoutInflater)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            //cela signifie que nous consommons les insets pour
            WindowInsetsCompat.CONSUMED
        }
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Retour à l'activité précédente
        }

        // Initialisation de la liste
        adapter = RappelAdapter(rappels,
            onDelete = { rappel ->
                userRepository.supprimerRappel(userId, rappel.id,
                    onSuccess = {
                        rappels.remove(rappel)
                        adapter.notifyDataSetChanged()
                        annulerNotification(rappel)
                    },
                    onError = { showToast(it) }
                )
            },
            onActiveChanged = { rappel, isActive ->
                // TODO: activer/désactiver l'alarme
            }
        )
        binding.recyclerViewRappels.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRappels.adapter = adapter

        // Charger les rappels Firestore au démarrage
        chargerRappelsFirestore()

        // Ouvrir le bottom sheet à l'appui sur le FAB
        binding.fabAddRappel.setOnClickListener {
            val btsheet = AddRappelBottomSheet { rappel ->
                userRepository.ajouterRappel(userId, rappel,
                    onSuccess = {
                        rappels.add(rappel)
                        adapter.notifyDataSetChanged()
                        planifierNotification(rappel)
                    },
                    onError = { showToast(it) }
                )
            }
            btsheet.show(supportFragmentManager, "AddRappelBottomSheet")
        }
    }

    private fun checkAlarmPermission(): Boolean {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
    private fun requestAlarmPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun chargerRappelsFirestore() {
        userRepository.getRappels(userId,
            onSuccess = {
                rappels.clear()
                rappels.addAll(it)
                adapter.notifyDataSetChanged()
                it.forEach { rappel -> planifierNotification(rappel) }
            },
            onError = { showToast(it) }
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun planifierNotification(rappel: Rappel) {
        if (!checkAlarmPermission()) {
            requestAlarmPermission()
            showToast("Veuillez autoriser les alarmes exactes")
            return
        }
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RappelReceiver::class.java)
        intent.putExtra("message", rappel.message)
        intent.putExtra("id", rappel.id)
        val pendingIntent = PendingIntent.getBroadcast(this, rappel.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val triggerAtMillis = rappel.timestamp
        if (rappel.repetition) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    private fun annulerNotification(rappel: Rappel) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RappelReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, rappel.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    private fun showToast(msg: String) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}