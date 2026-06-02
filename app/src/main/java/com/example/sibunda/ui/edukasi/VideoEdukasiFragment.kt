package com.example.sibunda.ui.edukasi

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sibunda.R
import com.example.sibunda.databinding.FragmentVideoEdukasiBinding

class VideoEdukasiFragment : Fragment() {

    private var _binding: FragmentVideoEdukasiBinding? = null
    private val binding get() = _binding!!

    private data class VideoEdukasi(
        val judul: String,
        val videoResId: Int
    )

    private val video1 = VideoEdukasi(
        judul = "Gizi Seimbang untuk Balita",
        videoResId = R.raw.video_gizi_1
    )

    private val video2 = VideoEdukasi(
        judul = "Pola Makan Sehat Anak",
        videoResId = R.raw.video_gizi_2
    )

    private val video3 = VideoEdukasi(
        judul = "Pemberian Makan Bayi dan Anak",
        videoResId = R.raw.video_gizi_3
    )

    private val video4 = VideoEdukasi(
        judul = "MPASI",
        videoResId = R.raw.video_gizi_4
    )

    private val video5 = VideoEdukasi(
        judul = "Pencegahan Stunting",
        videoResId = R.raw.video_gizi_5
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoEdukasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cardVideo1.setOnClickListener {
            bukaVideo(video1)
        }

        binding.cardVideo2.setOnClickListener {
            bukaVideo(video2)
        }

        binding.cardVideo3.setOnClickListener {
            bukaVideo(video3)
        }

        binding.cardVideo4.setOnClickListener {
            bukaVideo(video4)
        }

        binding.cardVideo5.setOnClickListener {
            bukaVideo(video5)
        }
    }

    private fun bukaVideo(video: VideoEdukasi) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_youtube_player)

        val tvJudulVideo = dialog.findViewById<TextView>(R.id.tvJudulVideo)
        val videoView = dialog.findViewById<VideoView>(R.id.videoView)
        val btnTutupVideo = dialog.findViewById<Button>(R.id.btnTutupVideo)
        val rootLayout = dialog.findViewById<View>(R.id.rootLayoutVideo)

        // Ubah warna teks judul agar terlihat di background gelap fullscreen
        tvJudulVideo.setTextColor(android.graphics.Color.WHITE)
        
        // Buat VideoView memenuhi layar
        val params = videoView.layoutParams
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        videoView.layoutParams = params

        tvJudulVideo.text = video.judul

        val resName = requireContext().resources.getResourceEntryName(video.videoResId)
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/raw/$resName")

        val mediaController = MediaController(requireContext())
        // Anchor ke rootLayout agar controller muncul di atas VideoView
        mediaController.setAnchorView(rootLayout)

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = false
            videoView.start()
            // Tampilkan controller sebentar saat mulai
            mediaController.show(3000)
            Toast.makeText(requireContext(), "Klik layar untuk Pause/Play", Toast.LENGTH_SHORT).show()
        }

        // Klik layar untuk toggle Play/Pause
        videoView.isClickable = true
        videoView.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
            } else {
                videoView.start()
            }
            mediaController.show(3000)
        }

        videoView.setOnErrorListener { _, what, extra ->
            Toast.makeText(requireContext(), "Gagal memutar video ($what)", Toast.LENGTH_SHORT).show()
            false
        }

        videoView.setOnCompletionListener {
            videoView.seekTo(0)
            mediaController.show(0)
        }

        btnTutupVideo.setOnClickListener {
            videoView.stopPlayback()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            videoView.stopPlayback()
        }

        dialog.show()

        // Maksimalkan ukuran dialog setelah muncul
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Sembunyikan navigasi dan status bar untuk mode imersif
        dialog.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
