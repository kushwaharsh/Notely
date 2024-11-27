package com.example.notesappwithhilt.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.PrefManager
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.DialogLayoutBinding
import com.example.notesappwithhilt.databinding.FragmentHomeBinding
import com.example.notesappwithhilt.models.Note
import com.example.notesappwithhilt.models.TagList
import com.example.notesappwithhilt.ui.adapters.GetAllNotesAdapter
import com.example.notesappwithhilt.ui.adapters.NoteTagAdapter
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var noteAdapter: GetAllNotesAdapter
    private lateinit var tagAdapter: NoteTagAdapter
    private var notesList = ArrayList<Note>()
    private var tagList = ArrayList<TagList>()
    private var filterTagList = ArrayList<Note>()

    // Note-color mapping
    private var noteColorMap = hashMapOf<String, String>()

    private var colorList = arrayListOf(
        "ffcccc", // Light Red
        "ccffcc", // Light Green
        "ccccff", // Light Blue
        "ffffcc", // Light Yellow
        "ccffff", // Light Cyan
        "ffccff", // Light Magenta
        "d3d3d3", // Light Gray
        "ffeb3b", // Light Orange
        "ffcc99", // Light Peach
        "e0f7fa"  // Light Cyan
    )

    @Inject
    lateinit var prefManager: PrefManager
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        observer()
        listener()

        return binding.root
    }

    private fun showSecurityLogoutDialog() {
        val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        val layoutDialog = BlurDialog(requireActivity(), R.style.TransparentDialogTheme)
        layoutDialog.setContentView(dialogBinding.root)

        layoutDialog.setCancelable(false)
        layoutDialog.setCanceledOnTouchOutside(false)

        dialogBinding.dialogTitle.text = "Security Alert ?"
        dialogBinding.dialogSubTitle.text = "Due to Security concerns your account will automatically logged out after 30 days. Please Login again !!"

        dialogBinding.btnCancel.visibility = View.GONE
        dialogBinding.btnConfirm.setText("Go to Login")
        dialogBinding.btnConfirm.setBackgroundResource(R.drawable.rounded_border_red)
        dialogBinding.btnConfirm.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        dialogBinding.btnConfirm.setOnClickListener {
            App.app.prefManager.clearPreferences()
            layoutDialog.dismiss()

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, true)
                .setLaunchSingleTop(true)
                .build()

            findNavController().navigate(R.id.action_homeFragment_to_loginFragment, null, navOptions)
        }

        layoutDialog.show()
    }


    private fun observer() {
        authViewModel.getAllNotes.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        notesList = it.value.notes as ArrayList<Note>

                        if (it.value.notes.isEmpty()) {
                            binding.createFirstNoteLayout.visibility = View.VISIBLE
                            binding.allNotesRV.visibility = View.GONE
                            binding.createNoteBtn.visibility = View.GONE
                            binding.tagBarRV.visibility = View.GONE
                        } else {
                            binding.createFirstNoteLayout.visibility = View.GONE
                            binding.allNotesRV.visibility = View.VISIBLE
                            binding.createNoteBtn.visibility = View.VISIBLE
                            binding.tagBarRV.visibility = View.VISIBLE
                        }

                        // Assign colors only if the map is empty (to maintain same colors on layout switch)
                        noteColorMap.clear()
                        notesList.forEachIndexed { index, note ->
                            noteColorMap[note._id] = colorList[index % colorList.size]
                        }

                        val noteColors = notesList.map { note ->
                            noteColorMap[note._id] ?: colorList[0] // Default color if not mapped
                        }

                        filterData("All", noteColors)
                    }

                }

                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                   showSecurityLogoutDialog()
                }

                null -> {}
            }
        }

        authViewModel.getAllTags.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        tagList = it.value.labels as ArrayList<TagList>
                        tagAdapter.setNewList(tagList)
                    }
                }

                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Something Went Wrong with tags",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                null -> {}
            }
        }
    }

    private fun filterData(tag: String, noteColors: List<String>) {
        if (tag.equals("All", ignoreCase = true)) {
            noteAdapter.setNewList(notesList, noteColors)
        } else {
            filterTagList =
                notesList.filter { tag.equals(it.tag, ignoreCase = true) } as ArrayList<Note>
            noteAdapter.setNewList(filterTagList, noteColors)
        }
    }

    private fun listener() {
        binding.createFirstNoteBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment)
        }

        binding.createNoteBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment)
        }

        binding.menuBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.notificationsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
        }

        binding.linearBtn.setOnClickListener {
            binding.gridBtn.visibility = View.VISIBLE
            binding.linearBtn.visibility = View.GONE
            setupLinearLayout()
        }

        binding.gridBtn.setOnClickListener {
            binding.linearBtn.visibility = View.VISIBLE
            binding.gridBtn.visibility = View.GONE
            setupGridLayout()
        }
    }

    private fun setupRecyclerView(layoutManager: RecyclerView.LayoutManager) {
        // Setup the RecyclerView with a layout manager and adapter
        binding.allNotesRV.layoutManager = layoutManager

        val noteColors = notesList.map { note ->
            noteColorMap[note._id] ?: colorList[0] // Ensure the color is retrieved from the map
        }

        noteAdapter = GetAllNotesAdapter(notesList, noteColors) { position ->
            val clickedNote = notesList[position]
            val clickedNoteId = clickedNote._id
            val clickedNoteColor =
                noteColorMap[clickedNoteId] ?: colorList[position % colorList.size]
            val bundle = Bundle().apply {
                putString("ClickedNoteId", clickedNoteId)
                putString("ClickedNoteColor", clickedNoteColor)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailedNoteFragment, bundle)
        }
        binding.allNotesRV.adapter = noteAdapter
    }

    private fun setupGridLayout() {
        // Setup grid layout for RecyclerView
        setupRecyclerView(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
    }

    private fun setupLinearLayout() {
        // Setup linear layout for RecyclerView
        setupRecyclerView(LinearLayoutManager(requireContext()))
    }

    private fun initView() {
        setupGridLayout()

        // Tag Adapter
        tagAdapter = NoteTagAdapter(tagList) { tag ->
            // Handle tag selection and filtering notes based on tag
            Log.d("selectedTag", tag)
            val noteColors = notesList.map { note ->
                noteColorMap[note._id] ?: colorList[0] // Default color if not mapped
            }
            filterData(tag, noteColors)
        }
        binding.tagBarRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.tagBarRV.adapter = tagAdapter

        binding.userNameTv.text = "Welcome Back ${prefManager.logginUserData?.name ?: "Guest"}"
        binding.useremailTv.text = prefManager.logginUserData?.email ?: "Unauthorized Access"


        //search Code here
        binding.searchbarTV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 noteAdapter.filter(s.toString() , binding.root)

                Log.d("onTextChange", s.toString())

            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("appToken", prefManager.accessToken ?: "")
        Log.d("userId", prefManager.logginUserData?._id ?: "")
        authViewModel.getAllNotes(
            prefManager.accessToken ?: "" ,
            prefManager.logginUserData?._id ?: ""
        )

        authViewModel.getAllTags(prefManager.accessToken.toString() , prefManager.logginUserData?.id.toString())
    }
}




