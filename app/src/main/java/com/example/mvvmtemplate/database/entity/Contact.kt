package com.example.mvvmtemplate.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val mobileNumber: String,
    val email: String,
    val category: Long,
    val profilePicUri: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(mobileNumber)
        parcel.writeString(email)
        parcel.writeLong(category)
        parcel.writeString(profilePicUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}
