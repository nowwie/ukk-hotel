package com.example.ukk.Transaksi

import android.widget.Spinner

data class TransaksiModel(
    var IdPesan: String?= null,
    var namaPesan: String?= null,
    var tglCheckIn: String?= null,
    var tglCheckOut: String?= null,
    var selectedItem: String?= null,
    var jmlKmr: String?= null,
//    var statusPesan: String?= null,
    var harga: String?= null
)
