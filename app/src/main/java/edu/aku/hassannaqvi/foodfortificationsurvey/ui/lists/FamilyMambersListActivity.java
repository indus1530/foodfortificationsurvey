package edu.aku.hassannaqvi.foodfortificationsurvey.ui.lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.ArrayList;

import edu.aku.hassannaqvi.foodfortificationsurvey.MainActivity;
import edu.aku.hassannaqvi.foodfortificationsurvey.R;
import edu.aku.hassannaqvi.foodfortificationsurvey.adapters.FamilyMembersAdapter;
import edu.aku.hassannaqvi.foodfortificationsurvey.contracts.TableContracts;
import edu.aku.hassannaqvi.foodfortificationsurvey.core.MainApp;
import edu.aku.hassannaqvi.foodfortificationsurvey.database.DatabaseHelper;
import edu.aku.hassannaqvi.foodfortificationsurvey.databinding.ActivityFamilyListBinding;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.FamilyMembers;
import edu.aku.hassannaqvi.foodfortificationsurvey.ui.EndingActivity;
import edu.aku.hassannaqvi.foodfortificationsurvey.ui.sections.SectionA2Activity;
import edu.aku.hassannaqvi.foodfortificationsurvey.ui.sections.SectionA31Activity;


public class FamilyMambersListActivity extends AppCompatActivity {


    private static final String TAG = "MwraActivity";
    ActivityFamilyListBinding bi;
    DatabaseHelper db;
    private FamilyMembersAdapter familyMembersAdapter;
    ActivityResultLauncher<Intent> MemberInfoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        //Intent data = result.getData();
                        Intent data = result.getData();
                      /*  int age = Integer.parseInt(femalemembers.getHh05y());
                        boolean isFemale = femalemembers.getHh03().equals("2");
                        boolean notMarried = femalemembers.getHh06().equals("2");
                        if (
                            // Adolescent: Male + Female - 10 to 19
                                (age >= 10 && age < 20 && notMarried)
                                        ||
                                        // MWRA: Married females between 14 to 49
                                        (age >= 14 && age < 50 && !notMarried && isFemale )

                        ) {*/
                        MainApp.familyList.add(MainApp.familyMember);
                        if (MainApp.familyMember.isMwra()) {
                            MainApp.mwraList.add(MainApp.familyList.size() - 1);
                            //MainApp.mwraCount++;
                        }
                        MainApp.memberCount++;
                        familyMembersAdapter.notifyItemInserted(MainApp.familyList.size() - 1);
                        //  Collections.sort(MainApp.fm, new SortByStatus());
                        //fmAdapter.notifyDataSetChanged();

                        //        }
                        checkCompleteFm();
                    }
                    if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(FamilyMambersListActivity.this, "No family member added.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mwra);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_family_list);
        bi.setCallback(this);

        db = MainApp.appInfo.dbHelper;
        MainApp.familyList = new ArrayList<>();
        MainApp.mwraList = new ArrayList<Integer>();
        Log.d(TAG, "onCreate: familyList " + MainApp.familyList.size());
        try {
            MainApp.familyList = db.getMemberBYUID(MainApp.form.getUid());
            int fmCount = 0;
            for (FamilyMembers fm : MainApp.familyList) {
                fmCount++;
                if (fm.isMwra()) {
                    MainApp.mwraList.add(fmCount - 1);
                    //MainApp.mwraCount++;
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSONException(FamilyMembers): " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        MainApp.selectedMWRA = "";
        // Set Selected MWRA
        for (int i = 0; i < MainApp.familyList.size(); i++) {
            if (MainApp.familyList.get(i).getIndexed().equals("1")) {
                MainApp.selectedMWRA = String.valueOf(i);

                bi.btnRand.setVisibility(View.INVISIBLE);
                bi.btnContinue.setVisibility(View.VISIBLE);
                break;
            }


        }

        bi.btnContinue.setEnabled(!MainApp.selectedMWRA.equals(""));
        bi.btnContinue.setVisibility(!MainApp.selectedMWRA.equals("") ? View.VISIBLE : View.INVISIBLE);
        MainApp.memberCount = Math.round(MainApp.familyList.size());

        familyMembersAdapter = new FamilyMembersAdapter(this, MainApp.familyList);
        bi.rvMwra.setAdapter(familyMembersAdapter);
        bi.rvMwra.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainApp.form.getiStatus().equals("1")) {
                    //     Toast.makeText(MwraActivity.this, "Opening Mwra Form", Toast.LENGTH_LONG).show();
                    MainApp.familyMember = new FamilyMembers();
                    addFemale();
                } else {
                    Toast.makeText(FamilyMambersListActivity.this, "This form has been locked. You cannot add new MWRA to locked forms", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Activity Resumed!", Toast.LENGTH_SHORT).show();

        //MainApp.familyMember = new MWRA();
        //MainApp.child = new Child();
        if (MainApp.mwraList.size() > 0 && MainApp.selectedMWRA.equals("")) {
            //MainApp.fm.get(Integer.parseInt(String.valueOf(MainApp.selectedMWRA))).setStatus("1");
            bi.btnRand.setVisibility(View.VISIBLE);
            bi.btnContinue.setVisibility(View.INVISIBLE);

        } else {
            bi.btnRand.setVisibility(View.INVISIBLE);
            bi.btnContinue.setVisibility(View.VISIBLE);
        }
        checkCompleteFm();


        // bi.fab.setClickable(!MainApp.form.getiStatus().equals("1"));
      /* bi.completedmember.setText(mwra.size()+ " MWRAs added");
        bi.totalmember.setText(MainApp.familyMemberTotal+ " M completed");*/
    }

    private void checkCompleteFm() {
        //     if (!MainApp.form.getIStatus().equals("1")) {
        int compCount = MainApp.familyList.size();

        MainApp.memberCountComplete = compCount;
        //   bi.btnContinue.setVisibility(compCount == memberCount && !form.getiStatus().equals("1")? View.VISIBLE : View.GONE);
     /*   bi.btnContinue.setVisibility(compCount >= memberCount ? View.VISIBLE : View.GONE);
        bi.btnContinue.setEnabled(bi.btnContinue.getVisibility()==View.VISIBLE);*/

        //  } else {
        //       Toast.makeText(this, "Form has been completed or locked", Toast.LENGTH_LONG).show();
        //   }
    }

    public void addFemale() {
        addMoreMember();

      /*  if (MainApp.familyList.size() >= Integer.parseInt(MainApp.form.getH220b())) {
            displayAddMoreDialog();
        } else {
            addMoreMWRA();

        }*/
    }

    public void btnContinue(View view) {

        try {
            MainApp.familyMember = db.getSelectedMemberBYUID(MainApp.form.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSONException(FamilyMembers): " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
        MainApp.familyList = new ArrayList<>();
        finish();
        startActivity(new Intent(this, !MainApp.familyMember.getIndexed().equals("1") ? EndingActivity.class : SectionA31Activity.class).putExtra("complete", true));

        //MainApp.familyMember = MainApp.familyList.get(Integer.parseInt(MainApp.selectedMWRA));

    /*    MainApp.familyMember = db.getSelectedMwraBYUID(MainApp.form.getUid());
        MainApp.familyList = new ArrayList<>();
        finish();
        startActivity(new Intent(this, !MainApp.familyMember.getIndexed().equals("1") ? EndingActivity.class : ChildListActivity.class).putExtra("complete", true));
    */
        /*else if (MainApp.familyList.size() < MainApp.memberCount) {

            Snackbar snackbar = Snackbar
                    .make(bi.parentLayout, "www.journaldev.com", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/

    }

    private void proceedSelect() {

        MainApp.selectedMWRA = MainApp.kishGrid(Integer.parseInt(MainApp.form.getSno()), MainApp.mwraList.size());
        int indx = MainApp.mwraList.get(Integer.parseInt(MainApp.selectedMWRA));

      /*  int aCount = 0;
        for (int i = 0; i < MainApp.familyList.size(); i++) {

            // Get MWRA from list
            MainApp.familyMember = MainApp.familyList.get(i);

            // Unselect and Disable if MWRA has been previously selected and refused
            if (MainApp.familyMember.getIndexed().equals("1")) {
                db.updatesfamilyListColumn(TableContracts.FamilyMemberListTable.COLUMN_INDEXED, "-1");
                MainApp.familyList.get(i).setIndexed("-1");
                familyMembersAdapter.notifyItemChanged(i);
            }

            // Count not refused and available mwra
            if (MainApp.familyMember.getIndexed().equals("") && MainApp.familyMember.getH227().equals("1")) {
                aCount++;
            }
        }

        if (aCount < 1) {
            Toast.makeText(this, "No MWRA available for selection.", Toast.LENGTH_LONG).show();
            MainApp.selectedMWRA = "";
            bi.btnRand.setVisibility(View.INVISIBLE);
            //bi.btnContinue.setVisibility(View.VISIBLE);
            return;

        }

        Random r = new Random();
        int indx = r.nextInt(MainApp.memberCount);
*/

        // Updating database to mark indexed mother
        MainApp.familyMember = MainApp.familyList.get(indx);
        db.updatesfamilyListColumn(TableContracts.FamilyMemberListTable.COLUMN_INDEXED, "1");

        // Updating adapter
        MainApp.familyList.get(indx).setIndexed("1");

        familyMembersAdapter.notifyItemChanged(indx);
        bi.btnRand.setVisibility(View.INVISIBLE);
        bi.btnContinue.setVisibility(View.VISIBLE);
        bi.btnContinue.setEnabled(true);


    }


  /*  private void displayAddMoreDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_mwra_dialog)
                .setMessage(String.format(getString(R.string.message_mwra_dialog_addmore), MainApp.form.getH220b()))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.h111a, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        addMoreMember();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.h111b, null)
                .setIcon(R.drawable.ic_alert_24)
                .show();

    }

    private void displayProceedDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_mwra_dialog)
                .setMessage(String.format(getString(R.string.message_mwra_dialog_proceeed), MainApp.familyList.size() + "", MainApp.form.getH220b()))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.h111a, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        proceedSelect();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.h111b, null)
                .setIcon(R.drawable.ic_alert_24)
                .show();

    }*/

    private void addMoreMember() {
        MainApp.familyMember = new FamilyMembers();
        Intent intent = new Intent(this, SectionA2Activity.class);
        //   finish();
        MemberInfoLauncher.launch(intent);
    }

    public void btnEnd(View view) {

        finish();
        startActivity(new Intent(this, MainActivity.class));
        /*   } else {
               Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show()
           }*/
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                //   mwra.get(selectedMWRA).setExpanded(false);
                checkCompleteFm();
                mwraAdapter.notifyItemChanged(selectedMWRA);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
                Toast.makeText(this, "Child for " + MainApp.familyList.get(selectedMWRA).getH221() + " was not added.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    public void btnRand(View view) {
/*        if (MainApp.familyList.size() < Integer.parseInt(MainApp.form.getH220b())) {
            displayProceedDialog();
        } else {*/
        proceedSelect();
        /*       }*/
    }
}