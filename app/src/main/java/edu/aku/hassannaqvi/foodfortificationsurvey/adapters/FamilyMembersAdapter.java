package edu.aku.hassannaqvi.foodfortificationsurvey.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.aku.hassannaqvi.foodfortificationsurvey.R;
import edu.aku.hassannaqvi.foodfortificationsurvey.core.MainApp;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.FamilyMembers;


public class FamilyMembersAdapter extends RecyclerView.Adapter<FamilyMembersAdapter.ViewHolder> {
    private static final String TAG = "MWRAAdapter";
    private final Context mContext;
    private final List<FamilyMembers> member;
    private final int mExpandedPosition = -1;
    private final int completeCount;
    private boolean motherPresent = false;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param members List<FemaleMembersModel> containing the data to populate views to be used by RecyclerView.
     */
    public FamilyMembersAdapter(Context mContext, List<FamilyMembers> members) {
        this.member = members;
        this.mContext = mContext;
        completeCount = 0;
        MainApp.memberComplete = false;


    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        FamilyMembers members = this.member.get(position);        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        TextView fName = viewHolder.fName;
        TextView fAge = viewHolder.fAge;
        // LinearLayout subItem = viewHolder.subItem;
        ImageView fmRow = viewHolder.fmRow;
        ImageView mainIcon = viewHolder.mainIcon;
        // TextView addSec = viewHolder.addSec;
        TextView fMaritalStatus = viewHolder.fMatitalStatus;
        TextView secStatus = viewHolder.secStatus;
        View cloaked = viewHolder.cloak;
        View indexedBar = viewHolder.indexedBar;
        TextView motherName = viewHolder.motherName;


        //String pregStatus = familyMember.getRb07().equals("1") ? "Pregnant" : "Not Pregnant";

        MainApp.memberComplete = completeCount == MainApp.memberCount;

        fName.setText(members.getA202());
        fAge.setText(members.getA206() + "y ");
        motherName.setText(null);
        String motherRelation = "";

/** Select mother IF
 *  Mother is alive and present in house
 */
        if (!members.getA213().equals("") && !members.getA213().equals("97")
        ) {
            if (members.getA204().equals("1")) {
                motherRelation = " S/o ";
            } else {
                motherRelation = " D/o ";

            }
            motherName.setText(motherRelation + MainApp.familyList.get(Integer.parseInt(members.getA213()) - 1).getA202());
            motherPresent = MainApp.familyList.get(Integer.parseInt(members.getA213()) - 1).getA211().equals("1");


        }

        String marStatus = "";
        switch (members.getA207t()) {
            case "1":
                marStatus = "Married";
                break;
            case "2":
                marStatus = "Unmarried";
                break;
            case "3":
                marStatus = "Widowed";
                break;
            case "4":
                marStatus = "Divorced/Separated";
                break;
            default:
                marStatus = "Value Unknown";
                break;
        }
        String idxStatus = "";

        int idxColor;
        switch (members.getIndexed()) {
            case "1":
                idxStatus = " Mother  ";
                idxColor = mContext.getResources().getColor(R.color.motherBg);
                break;
            case "2":
                idxStatus = "  Child  ";
                idxColor = mContext.getResources().getColor(R.color.childBg);
                break;
       /*     case "3":
                idxStatus = " Adol. M ";
                idxColor = mContext.getResources().getColor(R.color.adolMaleBg);
                break;
            case "4":
                idxStatus = " Adol. F ";
                idxColor = mContext.getResources().getColor(R.color.adolFemaleBg);
                break;*/

            default:
                idxStatus = "         ";
                idxColor = mContext.getResources().getColor(R.color.white);

                break;
        }

        fMaritalStatus.setText(marStatus);
        secStatus.setText(idxStatus);
        secStatus.setBackgroundColor(idxColor);

        cloaked.setVisibility(members.isMwra() ? View.GONE : View.VISIBLE);
        mainIcon.setImageResource(members.getA211().equals("1") ? ((members.getA204().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl)) : R.drawable.ic_not_available);
        //MainApp.selectedMWRA = members.getIndexed().equals("1") || members.getIndexed().equals("2") ? "-" : "";
        mainIcon.setBackgroundColor(members.getIndexed().equals("1") ? mContext.getResources().getColor(R.color.greenLight) : members.getIndexed().equals("2") ? mContext.getResources().getColor(android.R.color.holo_orange_dark) : members.getA204().equals("1") ? mContext.getResources().getColor(R.color.boy_blue) : mContext.getResources().getColor(R.color.girl_pink));
        //  mainIcon.setBackgroundColor(  ((ColorDrawable) mainIcon.getBackground()).getColor());
        if (!MainApp.selectedMWRA.equals("")) {
            cloaked.setVisibility(members.getIndexed().equals("") ? View.VISIBLE : View.GONE);
            indexedBar.setVisibility(members.getIndexed().equals("") ? View.GONE : View.VISIBLE);
        }
        //fMaritalStatus.setText("Children: " + familyMember.getH226m() + " boy(s), " + familyMember.getH226f() + " girl(s)");
       /* viewHolder.itemView.setOnClickListener(v -> {
            // Get the current state of the item

            MainApp.familyMember = MainApp.familyList.get(position);
            Intent intent = new Intent(mContext, SectionA2Activity.class);

            ((Activity) mContext).startActivityForResult(intent, 2);


        });*/

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.famlily_member_row, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return member.size();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fName;
        private final TextView fAge;
        private final TextView fMatitalStatus;
        private final TextView secStatus;
        //private final TextView addSec;
        //private final LinearLayout subItem;
        private final ImageView fmRow;
        private final ImageView mainIcon;
        private final View cloak;
        private final View indexedBar;
        private final TextView motherName;


        public ViewHolder(View v) {
            super(v);
            fName = v.findViewById(R.id.chh02);
            fAge = v.findViewById(R.id.chh05);
            fMatitalStatus = v.findViewById(R.id.chh06);
            secStatus = v.findViewById(R.id.csecStatus);
            //  addSec = v.findViewById(R.id.cadd_section);
            //  subItem = v.findViewById(R.id.csubitem);
            fmRow = v.findViewById(R.id.cfmRow);
            mainIcon = v.findViewById(R.id.mainIcon);
            cloak = v.findViewById(R.id.cloaked);
            indexedBar = v.findViewById(R.id.indexedBar);
            motherName = v.findViewById(R.id.chh08);

        }

        public TextView getTextView() {
            return fName;
        }
    }


}
