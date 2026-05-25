package com.example.sibunda.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.sibunda.core.data.local.entity.Pertumbuhan
import java.io.File
import java.io.FileOutputStream

object PdfHelper {

    fun generatePdf(

        context: Context,
        namaAnak: String,
        namaIbu: String,
        data: List<Pertumbuhan>

    ) {

        val pdfDocument = PdfDocument()

        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(
            1200,
            2010,
            1
        ).create()

        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        var y = 100

        paint.textSize = 40f
        paint.isFakeBoldText = true

        canvas.drawText(
            "LAPORAN RIWAYAT GIZI BALITA",
            300f,
            y.toFloat(),
            paint
        )

        y += 80

        paint.textSize = 30f
        paint.isFakeBoldText = false

        canvas.drawText(
            "Nama Anak : $namaAnak",
            100f,
            y.toFloat(),
            paint
        )

        y += 50

        canvas.drawText(
            "Nama Ibu : $namaIbu",
            100f,
            y.toFloat(),
            paint
        )

        y += 80

        paint.isFakeBoldText = true

        canvas.drawText(
            "Riwayat Pertumbuhan:",
            100f,
            y.toFloat(),
            paint
        )

        y += 60

        paint.isFakeBoldText = false

        data.forEachIndexed { index, item ->

            canvas.drawText(
                "${index + 1}. Umur ${item.umur} bulan",
                120f,
                y.toFloat(),
                paint
            )

            y += 40

            canvas.drawText(
                "BB : ${item.berat} kg",
                150f,
                y.toFloat(),
                paint
            )

            y += 40

            canvas.drawText(
                "TB : ${item.tinggi} cm",
                150f,
                y.toFloat(),
                paint
            )

            y += 40

            canvas.drawText(
                "Status : ${item.statusgizi}",
                150f,
                y.toFloat(),
                paint
            )

            y += 70
        }

        pdfDocument.finishPage(page)

        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )

        val file = File(
            downloadsFolder,
            "Riwayat_${namaAnak}.pdf"
        )

        try {

            pdfDocument.writeTo(
                FileOutputStream(file)
            )

            Toast.makeText(
                context,
                "PDF berhasil disimpan di Download",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                context,
                "Gagal membuat PDF",
                Toast.LENGTH_LONG
            ).show()
        }

        pdfDocument.close()
    }
}