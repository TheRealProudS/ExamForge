package de.sysitprep.app.ui.quiz

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic

public data class QuizFragmentArgs(
  public val sessionType: String,
  public val fachrichtung: String,
  public val lernfeld: Int = -1,
  public val examType: String = "AP1",
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("sessionType", this.sessionType)
    result.putInt("lernfeld", this.lernfeld)
    result.putString("fachrichtung", this.fachrichtung)
    result.putString("examType", this.examType)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("sessionType", this.sessionType)
    result.set("lernfeld", this.lernfeld)
    result.set("fachrichtung", this.fachrichtung)
    result.set("examType", this.examType)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): QuizFragmentArgs {
      bundle.setClassLoader(QuizFragmentArgs::class.java.classLoader)
      val __sessionType : String?
      if (bundle.containsKey("sessionType")) {
        __sessionType = bundle.getString("sessionType")
        if (__sessionType == null) {
          throw IllegalArgumentException("Argument \"sessionType\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"sessionType\" is missing and does not have an android:defaultValue")
      }
      val __lernfeld : Int
      if (bundle.containsKey("lernfeld")) {
        __lernfeld = bundle.getInt("lernfeld")
      } else {
        __lernfeld = -1
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
      val __examType : String?
      if (bundle.containsKey("examType")) {
        __examType = bundle.getString("examType")
        if (__examType == null) {
          throw IllegalArgumentException("Argument \"examType\" is marked as non-null but was passed a null value.")
        }
      } else {
        __examType = "AP1"
      }
      return QuizFragmentArgs(__sessionType, __fachrichtung, __lernfeld, __examType)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): QuizFragmentArgs {
      val __sessionType : String?
      if (savedStateHandle.contains("sessionType")) {
        __sessionType = savedStateHandle["sessionType"]
        if (__sessionType == null) {
          throw IllegalArgumentException("Argument \"sessionType\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"sessionType\" is missing and does not have an android:defaultValue")
      }
      val __lernfeld : Int?
      if (savedStateHandle.contains("lernfeld")) {
        __lernfeld = savedStateHandle["lernfeld"]
        if (__lernfeld == null) {
          throw IllegalArgumentException("Argument \"lernfeld\" of type integer does not support null values")
        }
      } else {
        __lernfeld = -1
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
      val __examType : String?
      if (savedStateHandle.contains("examType")) {
        __examType = savedStateHandle["examType"]
        if (__examType == null) {
          throw IllegalArgumentException("Argument \"examType\" is marked as non-null but was passed a null value")
        }
      } else {
        __examType = "AP1"
      }
      return QuizFragmentArgs(__sessionType, __fachrichtung, __lernfeld, __examType)
    }
  }
}
