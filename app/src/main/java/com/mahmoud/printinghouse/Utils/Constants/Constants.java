package com.mahmoud.printinghouse.Utils.Constants;


import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.models.local.ShieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int GALLERY = 3, CAMERA = 2 ;
    public static final int permission_camera_code=786;
    public static final int permission_write_data=788;
    public static final int permission_read_data =789;
    public static final int Select_image_from_gallery_code=3;
    public static final int REQUEST_VIDEO_CAPTURE = 109;
    public static final int PICKFILE_REQUEST_CODE = 141;
    public static final int SELECT_VIDEO = 110;
    public static final int PERMISSION_ACCESS_LOCATION = 111;
    public static final int RECORD_AUDIO_REQUEST_CODE = 101;
    public static boolean IS_VIDEO ;
    public static final String USER = "user" ;
    public static final String ADMIN = "admin";
    public static String CURRENT_ROLE ;
    public static boolean IS_FILE;
    public static final List<ShieldType> shields = Arrays.asList(
            new ShieldType("Shields","",-1),
            new ShieldType("تَعْجِز كَلِمَات الشُّكْرِ إنْ توفيك حَقَّك شُكْرًا\n" +
                    "و تَقْتَصِر دُونَك هامات التَّقْدِير انتحاذيك قَدْرًا\n" +
                    "عِنْدَهَا تَذُوب عِبَارَات الشُّكْر و التَّقْدِيرُ فِي بَحْرِ عَطَاءَك"
                    , "https://sketchfab.com/models/a9895985a23e426d93a7e24d4e733d8b/embed"
                    , R.drawable.ic_thanks),
            new ShieldType("للنجاحات أُنَاسٌ يَقْدِرُون مَعْنَاهُ وَ للابداع أُنَاسٌ يحصدونه لِذَا نَقْدِر جهودك المضنيه فَأَنْت أَهْل لِلشُّكْر و التَّقْدِير فَوَجَبَ عَلَيْنَا تَقْدِيرِك"
                    , "https://sketchfab.com/models/4c6f7e08df5f4444ab684eba682151f2/embed"
                    , R.drawable.ic_success),
            new ShieldType("رَائِع أَنْ يَضَعَ الْإِنْسَانُ هَدَفًا فِي حَيَاتِهِ و الاحلي أَنْ يُثْمِرَ ذَلِك الهَدَف طَمُوحًا يُسَاوِي طموحك . . .\n" +
                    "لِذَا تَسْتَحِقّ مِنَّا كُلَّ كَلِمَاتِ الشُّكْر بِعَدَد قَطَرَات الْمَطَر "
                    ,"https://sketchfab.com/models/f40ccd7ab7f6468fadb1dfafbebc80f1/embed"
                    , R.drawable.ic_owsom)
    );
    public static final List<String> PAPER_SIZE = Arrays.asList("Paper Size","A3","A4","A5");//
    public static final List<String> PAPER_TYPE = Arrays.asList(
            "Paper Type",
            "newsprint",
            "Writing and writing paper",
            "Crystal paper",
            "Cardboard",
            "Cushitic paper",
            "Duplex paper");

}
