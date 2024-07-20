package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val themePreference:ListPreference?=findPreference(getString(R.string.pref_key_dark))
        themePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val stringValue=newValue.toString()
            val nightMode=when(stringValue){
                getString(R.string.pref_dark_auto)->AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                getString(R.string.pref_dark_on)->AppCompatDelegate.MODE_NIGHT_YES
                getString(R.string.pref_dark_off)->AppCompatDelegate.MODE_NIGHT_NO
                else->AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            updateTheme(nightMode)
            true
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val switchPreference:SwitchPreference?=findPreference(getString(R.string.pref_key_notify))
        switchPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val broadcast= DailyReminder()
            if(newValue==true){
                broadcast.setDailyReminder(requireContext())
                Toast.makeText(activity,"Notification enabled",Toast.LENGTH_SHORT).show()
            }else{
                broadcast.cancelAlarm(requireContext())
                Toast.makeText(activity,"Notification disabled",Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}