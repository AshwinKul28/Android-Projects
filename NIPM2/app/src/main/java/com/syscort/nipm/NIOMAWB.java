package com.syscort.nipm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NIOMAWB extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niomawb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(NIOMAWB.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View xyz = null;
            View rootView = inflater.inflate(R.layout.fragment_niomawb, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                xyz = inflater.inflate(R.layout.awdchapter, container, false);
                TextView body = (TextView) xyz.findViewById(R.id.info);
                body.setText(Html.fromHtml("<b><u>NIPM Aurangabad Chapter:</u></b><br /><br/><small>National Institute Of Personnel Management (NIPM) is a Non Profit making body of HR professionals and we organize regular" +
                        " monthly meetings on different topics of common interest for the benefit of HR / Industry " +
                        "professionals from Aurangabad.<br />" +
                        "NIPM Aurangabad Chapter is one of the most vibrant chapters who has won several accolades at the national level including consecutive best chapter award from last 3 years in various categories of NIPM. It regularly conducts meetings, programs, symposiums, knowledge sharing sessions for students, professionals, " +
                        "veterans and family members of HR professionals as well.<br />" +
                        "We, At Aurangabad level have more than 400 HR professionals as members in various categories of memberships of HR professionals, " +
                        "service providers, Institutions and student members.<br />" +
                        "Prior to the formation of NIPM the Aurangabad chapter was a part of the erstwhile “National Institute of Labour Management (NILM). The chapter was inaugurated on 10th March 1980, by late Mr. Anand Bhadkamkar, the then Managing Director of Marathwada Development Corporation. " +
                        "The following were elected on the first Executive Committee.<br />" +
                        "Mr. S.G. Gokhale (Retd IG), President, Mr. V.P. Khanolkar, Chairman, Mr. Babu Marlapalle, Secretary (Now Hon. Judge, Mumbai High Court, Members, Mr. M.A. Mirza, Mr. A.P. Deshpande, Mr. B.A. Siddiqui, Mr. V.N. Bindoor, Mr. A.B. Kulkarni, " +
                        "Mr. N.N. Tiwantane, Mr. Z. Varsi & Smt. Shakuntala Lahoti.<br />" +
                        "Among the dignitaries present on the occasion of the inauguration were Mr. P.P. Kulkarni, the Chairman of NILM, Mr. M.S. Naik, Advocate, Mumbai, Mr. P.V. Karnik, Retd. Personnel Manager, The Times of India, Mumbai, Mr. Vijayendra Kabra, Renowned Union Leader, Aurangabad, all the Personnel Professionals and unit heads from Aurangabad. This chapter functioned till NILM and IIPM (Indian Institute of Personnel Management) merged to form NIPM (National Institute of Personnel Management) " +
                        "in the year 1980 at Chenna</small>"));
                Typeface mytypeface2;
                mytypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/trebuc.ttf");
                body.setTypeface(mytypeface2);


            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                xyz = inflater.inflate(R.layout.vision, container, false);
                TextView body = (TextView) xyz.findViewById(R.id.info);
                body.setText(Html.fromHtml("<b><h6><u>VISION & MISSION:</u></h6></b><h6>NIPM firmly believes in the dignity of human beings at " +
                        "work and their relationship within the enterprise. Keeping this in mind, NIPM is striving :</h6>" +
                        "<small>1.To spread the message of Professional Management in Human Resource Management and Development.<br/><br/>" +
                        "2.To promote an awareness of Professional Personnel Management at all levels in different organisations." +
                        "<br/><br/>3.To organise activities and programmes at both the national and chapter level with a view to upgrade the skills and professional standards of its members." +
                        "</small><br/><br/><b><u>OBJECTIVES:</u></b><br /><br/><small>1.To spread the knowledge on the approach, principles, practices, techniques and methods regarding personnel management, industrial management, " +
                        "industrial relations, labour & social welfare and industrial jurisprudence in all their bearings.<br/><br/>" +
                        "2.To serve as a forum for exchange of ideas and experiences and collection and dissemination of information on management in general and personnel management, industrial relations, " +
                        "human resources development and labour & social welfare, in particular.<br/><br/>" +
                        "3.To represent the Institute and its interests before Local, State and Central authorities and other organisations,commissions, boards, enquiry bodies etc., in India and abroad.<br/><br/>" +
                        "4.To promote and safeguard the status and the interests of personnel management, industrial relations and labour welfare at work places and the interests of those engaged therein.<br/></small>"));
                Typeface mytypeface2;
                mytypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/trebuc.ttf");
                body.setTypeface(mytypeface2);


            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                xyz = inflater.inflate(R.layout.team, container, false);
                TextView body = (TextView) xyz.findViewById(R.id.info);
                body.setText(Html.fromHtml("<b><h6><u>OUR TEAM:</u></h6></b>" +
                        "<small>1. Chairman : Dr.V.V.Rakhunde<br/>" +
                        "2. Vice Chairman : Mr.D.V.Suryawanshi<br/>" +
                        "3. Vice Chairman : Mr.Sunil P. Sutawane<br/>" +
                        "4. Hon.Secretary : Mr.Rajesh.B.Jawlekar<br/>" +
                        "5. Hon. Additional Secretary : Mr.Anurag.G.Kalyani<br/>" +
                        "6. Hon.Treasurer : Mr.Arun V.Pathak<br/>" +
                        "7. E C Member : Ms.Anjali. Bhat<br/>" +
                        "8. E C Member : Ms.Suchitra S.Mendke<br/>" +
                        "9. E C Member : Mr.Shivaji.G.Patil<br/>" +
                        "10. E C Member : Mr.Pareekshit D.Kale<br/>" +
                        "11. E C Member : Mr.Ashish.P.Wagh<br/>" +
                        "12. Co-opted : Mr.Pramod.M.Takwale<br/>" +
                        "13. Co-opted : Mr.Sanjay.N.Kapate<br/>" +
                        "14. Co-opted : Mr.Hrishikesh M.Aponarayan<br/>" +
                        "15. Co-opted : Mr.Nagesh.B. Deshpande<br/>" +
                        "16. Ex-Officio : Mr.Ram.H.Marlapalle<br/>" +
                        "17. Regional VP : Mr.Manoj Gupta<br/>" +
                        "18. Special Invitee : Mr.Milind.L.Patil<br/>" +
                        "19. Special Invitee : Mr.Rajesh.P.Wani<br/>" +
                        "20. Special Invitee : Mr.Puneet Dhingra<br/>" +
                        "21. Special Invitee : Adv.Ulhas.S.Sawji<br/>" +
                        "22. Special Invitee : Mr.S.G.Patil<br/>" +
                        "23. Special Invitee : Mr.Balaji Muley<br/>" +
                        "24. Special Invitee : Mr.Sanjay Gadhe<br/>" +
                        "25. Special Invitee : Mr.Gopal Kashte<br/>" +
                        "26. Patron : Mr.Makarand Deshpande<br/>" +
                        "27. Patron : Mr.Ulhas Bhoomkar</small>"));
                Typeface mytypeface2;
                mytypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/trebuc.ttf");
                body.setTypeface(mytypeface2);


            }

            return xyz;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ABOUT US";
                case 1:
                    return "VISION";
                case 2:
                    return "OUR TEAM";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NIOMAWB.this, MainActivity.class);
        startActivity(i);
        finish();

    }
}
