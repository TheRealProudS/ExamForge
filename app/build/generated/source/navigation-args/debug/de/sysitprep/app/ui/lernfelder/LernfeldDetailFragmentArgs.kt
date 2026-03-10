package de.sysitprep.app.ui.lernfelder

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic

public data class LernfeldDetailFragmentArgs(
  public val lernfeldNumber: Int,
  public val fachrichtung: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("lernfeldNumber", this.lernfeldNumber)
    result.putString("fachrichtung", this.fachrichtung)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("lernfeldNumber", this.lernfeldNumber)
    result.set("fachrichtung", this.fachrichtung)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): LernfeldDetailFragmentArgs {
      bundle.setClassLoader(LernfeldDetailFragmentArgs::class.java.classLoader)
      val __lernfeldNumber : Int
      if (bundle.containsKey("lernfeldNumber")) {
        __lernfeldNumber = bundle.getInt("lernfeldNumber")
      } else {
        throw IllegalArgumentException("Required argument \"lernfeldNumber\" is missing and does not have an android:defaultValue")
      }
      val __fachrichtung : String?
      if (bundle.containsKey("fachrichtung")) {
        __fachrichtung = bundle.getString("fachrichtung")
        if (__fachrichtung == null) {
          throw IllegalArgumentException("Argument \"fachrichtung\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"fachrichtung\" is missing and does not have an android:defaultValue")
      }
      return LernfeldDetailFragmentArgs(__lernfeldNumber, __fachrichtung)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        LernfeldDetailFragmentArgs {
      val __lernfeldNumber : Int?
      if (savedStateHandle.contains("lernfeldNumber")) {
        __lernfeldNumber = savedStateHandle["lernfeldNumber"]
        if (__lernfeldNumber == null) {
          throw IllegalArgumentException("Argument \"lernfeldNumber\" of type integer does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"lernfeldNumber\" is missing and does not have an android:defaultValue")
      }
      val __fachrichtung : String?
      if (savedStateHandle.contains("fachrichtung")) {
        __fachrichtung = savedStateHandle["fachrichtung"]
        if (__fachrichtung == null) {
          throw IllegalArgumentException("Argument \"fachrichtung\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"fachrichtung\" is missing and does not have an android:defaultValue")
      }
      return LernfeldDetailFragmentArgs(__lernfeldNumber, __fachrichtung)
    }
  }
}
