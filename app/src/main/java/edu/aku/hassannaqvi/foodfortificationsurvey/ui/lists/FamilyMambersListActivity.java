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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;

import java.util.ArrayList;

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
                    if (result.getResultCode() == Activity.RESULT_OK && !MainApp.superuser) {
                        Toast.makeText(FamilyMambersListActivity.this, "MemberInfoLauncher", Toast.LENGTH_SHORT).show();

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
                        MainApp.hhheadSelected = MainApp.familyMember.getA203t().equals("1");
                        //  memGender = MainApp.familyMember.getA204();
                        // boolean memAgeCheck = Integer.parseInt(MainApp.familyMember.getA206()) > 18;
                        if (!MainApp.familyMember.getA207t().equals("2")) {
                            switch (MainApp.familyMember.getA204()) {
                                case "1":
                                    MainApp.fatherList.add(MainApp.familyMember);
                                    //MainApp.mwraCount++;
                                    break;
                                case "2":
                                    MainApp.motherList.add(MainApp.familyMember);
                                    //MainApp.adolCount++;
                                    break;

                            }
                        }
                        String motherSno = MainApp.familyMember.getA213();
                        if (!motherSno.equals("")
                                && !motherSno.equals("97")
                                && !MainApp.mwraList.contains(Integer.parseInt(motherSno))

                        ) {
                            FamilyMembers mother = MainApp.familyList.get(Integer.parseInt(motherSno) - 1);

                            if (mother.getA211().equals("1") && Integer.parseInt(mother.getA206()) >= 18) {
                                MainApp.mwraList.add(Integer.parseInt(motherSno));
                            }
                        }
                      /*  if (MainApp.familyMember.isMwra()) {
                            MainApp.mwraList.add(MainApp.familyList.size() - 1);
                            //MainApp.mwraCount++;
                        }*/
                        MainApp.memberCount++;
                        familyMembersAdapter.notifyItemInserted(MainApp.familyList.size() - 1);
                        //familyMembersAdapter.notifyItemInserted(Integer.parseInt(MainApp.familyMember.getA213())-1);
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
        if (MainApp.superuser)
            bi.btnContinue.setText("Review Next");
        db = MainApp.appInfo.dbHelper;
        MainApp.familyList = new ArrayList<>();
        MainApp.mwraList = new ArrayList<Integer>();
        MainApp.fatherList = new ArrayList<>();
        MainApp.motherList = new ArrayList<>();

        bi.hhNo.setText("Family of: " + MainApp.form.getEbCode() + "-" + MainApp.form.getHhid());
        Log.d(TAG, "onCreate: familyList " + MainApp.familyList.size());
        try {
            MainApp.familyList = db.getMemberBYUID(MainApp.form.getUid());
            int fmCount = 0;
            for (FamilyMembers fm : MainApp.familyList) {
                fmCount++;
                if (!fm.getA207t().equals("2")) {

                    switch (fm.getA204()) {
                        case "1":
                            MainApp.fatherList.add(fm);
                            //MainApp.mwraCount++;
                            break;
                        case "2":
                            MainApp.motherList.add(fm);
                            //MainApp.adolCount++;
                            break;

                    }
                }

                String motherSno = fm.getA213(); // mother's line number from child
                if (!motherSno.equals("") && !motherSno.equals("97") && !MainApp.mwraList.contains(Integer.parseInt(motherSno))) {
                   // MainApp.mwraList.add(Integer.parseInt(motherSno));
                    FamilyMembers mother = MainApp.familyList.get(Integer.parseInt(motherSno) - 1);

                    if (mother.getA211().equals("1") && Integer.parseInt(mother.getA206()) >= 18) {
                        MainApp.mwraList.add(Integer.parseInt(motherSno));
                    }
                }
        /*        if (fm.isMwra()) {
                    MainApp.mwraList.add(fmCount - 1);
                    //MainApp.mwraCount++;
                }*/

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSONException(FamilyMembers): " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        MainApp.selectedMWRA = "";
        MainApp.selectedChild = "";
        // Set Selected MWRA
        for (int i = 0; i < MainApp.familyList.size(); i++) {
            if (MainApp.familyList.get(i).getIndexed().equals("1")) {
                MainApp.selectedMWRA = String.valueOf(i);
                bi.btnRand.setVisibility(View.INVISIBLE);
                //bi.btnContinue.setVisibility(View.VISIBLE);
                bi.btnContinue.setEnabled(true);
                bi.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_shape_green));
                // break;
            }
            if (MainApp.familyList.get(i).getIndexed().equals("2"))
                MainApp.selectedChild = String.valueOf(i);
            MainApp.hhheadSelected = MainApp.familyList.get(i).getA203t().equals("1");

        }

        bi.familyComplete.setChecked(!MainApp.selectedMWRA.equals(""));
        bi.btnContinue.setEnabled(!MainApp.selectedMWRA.equals(""));
        bi.btnContinue.setBackground(!MainApp.selectedMWRA.equals("") ? getResources().getDrawable(R.drawable.button_shape_green) : getResources().getDrawable(R.drawable.button_shape_gray));

        //bi.btnContinue.setVisibility(!MainApp.selectedMWRA.equals("") ? View.VISIBLE : View.INVISIBLE);
        MainApp.memberCount = Math.round(MainApp.familyList.size());

        familyMembersAdapter = new FamilyMembersAdapter(this, MainApp.familyList);
        bi.rvMwra.setAdapter(familyMembersAdapter);
        bi.rvMwra.setLayoutManager(new LinearLayoutManager(this));


        bi.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApp.superuser) {
                    Toast.makeText(FamilyMambersListActivity.this, "Supervisors cannot add new members.", Toast.LENGTH_LONG).show();

                } else if ((!MainApp.form.getiStatus().equals("1") || MainApp.selectedMWRA.equals("")) && !bi.familyComplete.isChecked()) {
                    //     Toast.makeText(MwraActivity.this, "Opening Mwra Form", Toast.LENGTH_LONG).show();
                    MainApp.familyMember = new FamilyMembers();
                    addFemale();
                } else {
                    Toast.makeText(FamilyMambersListActivity.this, "This form has been locked. You cannot add new family member to locked forms", Toast.LENGTH_LONG).show();
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
      /*  if (MainApp.mwraList.size() > 0 && MainApp.selectedMWRA.equals("")) {
            //MainApp.fm.get(Integer.parseInt(String.valueOf(MainApp.selectedMWRA))).setStatus("1");
           bi.btnRand.setVisibility(View.VISIBLE);
            // bi.btnContinue.setVisibility(View.INVISIBLE);
            bi.btnContinue.setEnabled(false);
            bi.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_shape_gray));

        } else {
            bi.btnRand.setVisibility(View.INVISIBLE);
            // bi.btnContinue.setVisibility(View.VISIBLE);
            bi.btnContinue.setEnabled(true);
            bi.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_shape_green));

        }*/
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
        //MainApp.familyList = new ArrayList<>();
        finish();
        Intent i = new Intent(this, MainApp.selectedMWRA.equals("") ? EndingActivity.class : SectionA31Activity.class);
        i.putExtra("complete", !MainApp.selectedMWRA.equals(""));
        startActivity(i);

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


        // Select Index Mother using KishGrid
        String kishGridMWRA = MainApp.kishGrid(Integer.parseInt(MainApp.form.getSno()), MainApp.mwraList.size());
        int indx = MainApp.mwraList.get(Integer.parseInt(kishGridMWRA));

        // Updating database to mark indexed mother
        MainApp.selectedMWRA = String.valueOf(indx - 1);
        MainApp.familyMember = MainApp.familyList.get(Integer.parseInt(MainApp.selectedMWRA));
        db.updatesfamilyListColumn(TableContracts.FamilyMemberListTable.COLUMN_INDEXED, "1");

        // Updating adapter
        MainApp.familyList.get(Integer.parseInt(MainApp.selectedMWRA)).setIndexed("1");

        familyMembersAdapter.notifyItemChanged(Integer.parseInt(MainApp.selectedMWRA));
        int i = 0;

        MainApp.childOfSelectedMWRAList = new ArrayList<>();
        for (FamilyMembers fm : MainApp.familyList) {
            if (fm.getA213().equals(MainApp.familyMember.getA201())) {
                MainApp.childOfSelectedMWRAList.add(Integer.parseInt(fm.getA201()));
            }
        }

        String kishGridChild = MainApp.kishGrid(Integer.parseInt(MainApp.form.getSno()), MainApp.childOfSelectedMWRAList.size());
        indx = MainApp.childOfSelectedMWRAList.get(Integer.parseInt(kishGridChild));

        MainApp.selectedChild = String.valueOf((indx -1));
        MainApp.familyMember = MainApp.familyList.get(Integer.parseInt(MainApp.selectedChild));
        db.updatesfamilyListColumn(TableContracts.FamilyMemberListTable.COLUMN_INDEXED, "2");

        // Updating adapter
        MainApp.familyList.get(Integer.parseInt(MainApp.selectedChild)).setIndexed("2");

        familyMembersAdapter.notifyItemChanged(Integer.parseInt(MainApp.selectedChild));

        bi.btnRand.setVisibility(View.INVISIBLE);
        // bi.btnContinue.setVisibility(View.VISIBLE);
        bi.btnContinue.setEnabled(true);
        bi.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_shape_green));


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
        // startActivity(new Intent(this, MainActivity.class));
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
        if (MainApp.mwraList.size() > 0 && MainApp.selectedMWRA.equals("")) {
            proceedSelect();
        } else {
            bi.btnRand.setVisibility(View.VISIBLE);
        }
        /*       }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
            Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
        // A213 is line number
        familyMembersAdapter.notifyItemChanged(Integer.parseInt(MainApp.familyMember.getA201()) - 1);

    }

    public void finalizeFamily(View view) {
        if (MainApp.mwraList.size() > 0 && MainApp.selectedMWRA.equals("")) {
            //MainApp.fm.get(Integer.parseInt(String.valueOf(MainApp.selectedMWRA))).setStatus("1");
            bi.btnRand.setVisibility(View.VISIBLE);
            // bi.btnContinue.setVisibility(View.INVISIBLE);
            bi.btnContinue.setEnabled(false);
            bi.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_shape_gray));

        } else {
            bi.btnRand.setVisibility(View.INVISIBLE);
            // bi.btnContinue.setVisibility(View.VISIBLE);
            bi.btnContinue.setEnabled(true);
            bi.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_shape_green));

        }
    }


    @Override
    public void onBackPressed() {

        // Allow BackPress
/*        setResult(Activity.RESULT_CANCELED);
        finish();
        super.onBackPressed();*/

//         Dont Allow BackPress
        Toast.makeText(this, "Back Press Not Allowed", Toast.LENGTH_SHORT).show();

    }
}