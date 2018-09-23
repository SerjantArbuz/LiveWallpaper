package sgtmelon.livewallpaper.app.view.act;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.adapter.AdpWallpaper;
import sgtmelon.livewallpaper.app.control.CtrlMenu;
import sgtmelon.livewallpaper.app.database.DbRoom;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.app.viewModel.VmWallpaper;
import sgtmelon.livewallpaper.databinding.ActWallpaperBinding;
import sgtmelon.livewallpaper.element.DlgSheet;
import sgtmelon.livewallpaper.office.Help;
import sgtmelon.livewallpaper.office.annot.DefCode;
import sgtmelon.livewallpaper.office.annot.DefDlg;
import sgtmelon.livewallpaper.office.annot.DefMode;
import sgtmelon.livewallpaper.office.annot.DefPage;
import sgtmelon.livewallpaper.office.intf.IntfItem;
import sgtmelon.livewallpaper.office.st.StMode;
import sgtmelon.livewallpaper.office.st.StOpen;

public class ActWallpaper extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, IntfItem.Click, IntfItem.LongClick {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    }

    private static final String TAG = "ActWallpaper";

    //region Variable
    private ActWallpaperBinding binding;
    private VmWallpaper vm;

    private FragmentManager fm;

    private StOpen stOpen;
    private StMode stMode;
    //endregion

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
        adpWallpaper.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.act_wallpaper);
        vm = ViewModelProviders.of(this).get(VmWallpaper.class);

        fm = getSupportFragmentManager();

        setupNavigation();
        setupRecycler();

        stOpen = new StOpen();
        stMode = new StMode();

        if (savedInstanceState != null) {
            stOpen.setOpen(savedInstanceState.getBoolean(DefDlg.OPEN));

            stMode.setActive(savedInstanceState.getBoolean(DefMode.ACTIVE));
            stMode.setSelect(savedInstanceState.getBooleanArray(DefMode.SELECT));
            stMode.setCount(savedInstanceState.getInt(DefMode.COUNT));
        }

        ctrlMenu.changeMode(savedInstanceState != null && stMode.isActive(), stMode.getCount());
    }

    private DlgSheet dlgSheetAdd;
    private CtrlMenu ctrlMenu;

    private void setupNavigation() {
        Log.i(TAG, "setupNavigation");

        Toolbar toolbar = findViewById(R.id.incToolbar_tb);
        toolbar.inflateMenu(R.menu.menu_act_main);
        toolbar.setOnMenuItemClickListener(this);

        dlgSheetAdd = (DlgSheet) fm.findFragmentByTag(DefDlg.SHEET_ADD);
        if (dlgSheetAdd == null) dlgSheetAdd = new DlgSheet();

        dlgSheetAdd.setArguments(R.layout.sheet_add, R.id.sheetAdd_nv_menu);
        dlgSheetAdd.setDismissListener(dialogInterface -> stOpen.setOpen(false));
        dlgSheetAdd.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.actMain_fab);
        fab.setOnClickListener(view -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen();
                dlgSheetAdd.show(fm, DefDlg.SHEET_ADD);
            }
        });

        ctrlMenu = new CtrlMenu(this, toolbar, fab);
        ctrlMenu.setNavClickListener(view -> {
            ctrlMenu.changeMode(false, 0);

            stMode = new StMode(vm.getListWallpaper().size());
            adpWallpaper.setStMode(stMode);
            adpWallpaper.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_actMain_delete:
                boolean[] select = adpWallpaper.getStMode().getSelect();

                List<ItemWallpaper> listAll = vm.getListWallpaper();
                List<ItemWallpaper> listDelete = new ArrayList<>();

                for (int i = 0; i < select.length; i++) {
                    if (select[i]) {
                        ItemWallpaper itemWallpaper = listAll.get(i);
                        listDelete.add(itemWallpaper);

                        boolean delete = Help.Data.deleteFile(itemWallpaper.getPath());

                        if (delete) {
                            Log.i(TAG, "onMenuItemClick: delete");
                        } else {
                            Log.i(TAG, "onMenuItemClick: not delete");
                        }
                    }
                }

                DbRoom dbRoom = DbRoom.provideDb(this);
                dbRoom.daoWallpaper().delete(listDelete);
                dbRoom.close();

                ctrlMenu.changeMode(false, 0);

                stMode = new StMode(vm.getListWallpaper().size());

                updateAdapter();
                adpWallpaper.notifyDataSetChanged();
                break;
            case R.id.menu_actMain_info:
                intent = new Intent(ActWallpaper.this, ActInfo.class);
                startActivity(intent);
                return true;
            case R.id.menu_actMain_preference:
                intent = new Intent(ActWallpaper.this, ActPreference.class);
                startActivity(intent);
                return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        dlgSheetAdd.dismiss();

        switch (menuItem.getItemId()) {
            case R.id.menu_sheetAdd_camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, DefCode.camera);
                    } else {
                        startCameraIntent();
                    }
                } else {
                    startCameraIntent();
                }
                break;
            case R.id.menu_sheetAdd_gallery:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, DefCode.gallery);
                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult");

        switch (requestCode) {
            case DefCode.camera:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraIntent();
                }
                break;
        }
    }

    private void startCameraIntent() {
        Log.i(TAG, "startCameraIntent");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File file = null;
            try {
                file = Help.Data.createFile(this);
            } catch (IOException ex) {
                Toast.makeText(this, getString(R.string.message_file_error_complete), Toast.LENGTH_SHORT).show();
            }

            if (file != null) {
                Help.Data.URI = FileProvider.getUriForFile(this, getString(R.string.file_provider_name), file);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Help.Data.URI);
                startActivityForResult(intent, DefCode.camera);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");

        switch (requestCode) {
            case DefCode.camera:
                if (resultCode == RESULT_OK) {
                    if (Help.Data.URI != null) {
                        DbRoom db = DbRoom.provideDb(this);
                        db.daoWallpaper().insert(new ItemWallpaper(Help.Data.URI, Help.Data.PATH));
                        db.close();

                        updateAdapter();
                        adpWallpaper.notifyItemInserted(0);

                        Help.Data.URI = null;
                        Help.Data.PATH = null;

                        Toast.makeText(this, getString(R.string.message_file_save), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case DefCode.gallery:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            try {
                                Help.Data.copyFile(this, uri);

                                DbRoom db = DbRoom.provideDb(this);
                                db.daoWallpaper().insert(new ItemWallpaper(Help.Data.URI, Help.Data.PATH));
                                db.close();

                                updateAdapter();
                                adpWallpaper.notifyItemInserted(0);

                                Help.Data.URI = null;
                                Help.Data.PATH = null;
                            } catch (IOException e) {
                                Toast.makeText(this, getString(R.string.message_file_error_copy), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
        }
    }

    private AdpWallpaper adpWallpaper;

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        RecyclerView recyclerView = findViewById(R.id.actMain_rv);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.column_count));
        recyclerView.setLayoutManager(layoutManager);

        adpWallpaper = new AdpWallpaper();
        adpWallpaper.setClick(this);
        adpWallpaper.setLongClick(this);

        recyclerView.setAdapter(adpWallpaper);
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        adpWallpaper.update(vm.getListWallpaper());
        adpWallpaper.setStMode(stMode);

        binding.setSize(vm.getListWallpaper().size());
        binding.executePendingBindings();
    }

    //Нажатие на фото
    @Override
    public void onItemClick(int p) {
        Log.i(TAG, "onItemClick");

        if (stMode.isActive()) {
            stMode.setSelect(p);
            ctrlMenu.changeCount(stMode.getCount());
        } else {
            Intent intent = new Intent(ActWallpaper.this, ActFullscreen.class);
            intent.putExtra(DefPage.CURRENT, p);
            startActivity(intent);
        }
    }

    //Переключение мода выделения
    @Override
    public void onItemLongClick(int p) {
        Log.i(TAG, "onItemLongClick");

        if (!stMode.isActive()) {
            stMode.setActive(true);
            stMode.setSelect(p);

            adpWallpaper.setStMode(stMode);
            adpWallpaper.notifyDataSetChanged();

            ctrlMenu.changeMode(true, stMode.getCount());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putBoolean(DefDlg.OPEN, stOpen.isOpen());

        stMode = adpWallpaper.getStMode();

        outState.putBoolean(DefMode.ACTIVE, stMode.isActive());
        outState.putBooleanArray(DefMode.SELECT, stMode.getSelect());
        outState.putInt(DefMode.COUNT, stMode.getCount());
    }

}