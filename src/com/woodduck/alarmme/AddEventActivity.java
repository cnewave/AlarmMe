
package com.woodduck.alarmme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.woodduck.alarmme.database.ItemDAO;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

// Here we will change AddTaskActivity to support edit/add task .
public class AddEventActivity extends ActionBarActivity {
    private String TAG = "AlarmMeMain";
    private Button mDateButton;
    private Button mTimeButton;
    private Button mOKButton;
    private Button mCancelButton;
    private EditText mTitle;
    private EditText mDetail;
    private int queryID = 0;
    // icon chooser
    private ImageButton mIconChooser;

    Calendar rightNow;
    AudioFragment mAudioFragment;
    VideoFragment mVideoFragment;
    RadioButton mAudioRadio;
    RadioButton mVideoRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.add_event_task);
        initUI();
    }

    private void initFragments() {
        mVideoFragment = VideoFragment.newInstance();
        mAudioFragment = AudioFragment.newInstance();
    }

    private void initAudioFragement() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(mVideoFragment);
        ft.add(R.id.recorder_page, mAudioFragment);
        ft.commit();
    }

    private void initVideoFragement() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(mAudioFragment);
        ft.add(R.id.recorder_page, mVideoFragment);
        ft.commit();
    }

    private void initUI() {
        rightNow = Calendar.getInstance();
        initEditText();
        initFragments();
        // initAudioFragement();
        // initVideoFragement();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.add_actionbar);
        actionBar.show();

        initButton();
        initRadioButtons();
        getIntents();
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Width:" + mIconChooser.getWidth());
        Log.d(TAG, "Heigh:" + mIconChooser.getHeight());
    }

    private void initRadioButtons() {

        mAudioRadio = (RadioButton) findViewById(R.id.radio_audio);
        mAudioRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick audio fragment");
                initAudioFragement();
            }
        });
        mVideoRadio = (RadioButton) findViewById(R.id.radio_video);
        mVideoRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick video fragment");
                initVideoFragement();
            }
        });
        if (mAudioRadio.isChecked()) {
            initAudioFragement();
        } else {
            initVideoFragement();
        }
    }

    private void getIntents() {
        Intent intent = this.getIntent();
        if (intent != null) {
            queryID = intent.getIntExtra("_id", 0);
            Log.d(TAG, "query ID :" + queryID);
            if (queryID != 0) {
                ItemDAO readDAO = new ItemDAO(this);
                EventItem item = readDAO.get(queryID);
                Log.d(TAG, "Query item :" + item);
                mTitle.setText(item.getName());
                mDetail.setText(item.getDetail());
                String execute = item.getExecuteTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    rightNow.setTime(dateFormat.parse(execute));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String filePath = item.getVideoPath();
                if (filePath != null) {
                    mVideoFragment.setRecordPath(filePath);
                }
                filePath = item.getAudioPath();
                if (filePath != null) {
                    mAudioFragment.setRecordPath(filePath);
                }
                mCurrentPhotoPath = item.getPicturePath();
                String path = Environment.getExternalStorageDirectory().toString();
                Log.d(TAG, "Query... :" + mCurrentPhotoPath + " path:" + path);
                /*
                 * InputStream is = assetManager.open(files[index]); bitmap = BitmapFactory.decodeStream(is);
                 */

                if (mCurrentPhotoPath != null) {
                    if (!mCurrentPhotoPath.contains(path)) {
                        Log.d(TAG, "Get from asset");
                        AssetManager assetManager = getAssets();

                        try {
                            InputStream is = assetManager.open(mCurrentPhotoPath);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            mIconChooser.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        setPic();
                    }
                }
            }
        } else {
            Log.d(TAG, "No inten.. add activity");
        }

    }

    private void initButton() {
        mDateButton = (Button) findViewById(R.id.datechooser);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch date picker.
                launchDatePicker();
            }
        });
        mDateButton.setText(getToday());

        mTimeButton = (Button) findViewById(R.id.timechooser);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch time picker
                launchTimePicker();
            }
        });
        mTimeButton.setText(getCurrentTime());

        mOKButton = (Button) findViewById(R.id.ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                saveTask();
            }
        });
        mCancelButton = (Button) findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTask();
            }
        });

        mIconChooser = (ImageButton) findViewById(R.id.iconchooser);
        mIconChooser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogWithGridView();
            }
        });

    }

    private void initEditText() {
        mTitle = (EditText) findViewById(R.id.title);
        mDetail = (EditText) findViewById(R.id.details);
    }

    private void saveTask() {
        //
        // Create a thread to save to DB.
        createEventItem();
        setResult(RESULT_OK);
        finish();
    }

    private void cancelTask() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private String getToday() {
        StringBuilder today = new StringBuilder(rightNow.get(Calendar.MONTH) + 1 + "/" + rightNow.get(Calendar.DATE));
        Log.d(TAG, "time :" + rightNow.getTime());
        Log.d(TAG, "Year :" + rightNow.get(Calendar.YEAR));
        Log.d(TAG, "Month :" + rightNow.get(Calendar.MONTH));
        Log.d(TAG, "Date :" + rightNow.get(Calendar.DATE));
        // Test
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(rightNow.getTime());
        Log.d(TAG, "time -->:" + currentDateandTime);
        return today.toString();
    }

    private String getCurrentTime() {
        StringBuilder currentTime = new StringBuilder(rightNow.get(Calendar.HOUR_OF_DAY) + ":"
                + String.format("%02d", rightNow.get(Calendar.MINUTE)));

        Log.d(TAG, "hour :" + rightNow.get(Calendar.HOUR_OF_DAY));
        Log.d(TAG, "minutes :" + rightNow.get(Calendar.MINUTE));
        return currentTime.toString();
    }

    private void launchDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, 0, mDateListenee, rightNow.get(Calendar.YEAR),
                rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DATE));
        dialog.show();
    }

    private void launchTimePicker() {
        TimePickerDialog dialog = new TimePickerDialog(this, 0, mTimePickeer, rightNow.get(Calendar.HOUR_OF_DAY),
                rightNow.get(Calendar.MINUTE), false);
        dialog.show();
    }

    DatePickListener mDateListenee = new DatePickListener();

    class DatePickListener implements OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d(TAG, "time :" + dayOfMonth);
            mDateButton.setText((monthOfYear + 1) + "/" + dayOfMonth);
            rightNow.set(Calendar.MONTH, monthOfYear);
            rightNow.set(Calendar.DATE, dayOfMonth);
        }
    }

    TimePickListener mTimePickeer = new TimePickListener();

    class TimePickListener implements OnTimeSetListener {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTimeButton.setText((hourOfDay) + ":" + minute);
            rightNow.set(Calendar.HOUR_OF_DAY, hourOfDay);
            rightNow.set(Calendar.MINUTE, minute);
        }
    }

    private void createEventItem() {
        try {
            String title = mTitle.getText().toString();
            String detail = mDetail.getText().toString();

            EventItem item = new EventItem(title, detail,
                    mAudioFragment.getRecordPath(),
                    mVideoFragment.getRecordPath(),
                    mCurrentPhotoPath,
                    getDateTime(rightNow.getTime()));
            item.setId(queryID);
            Log.d(TAG, "createEventItem :" + item);
            // testDB(item);
            prepareAlarmManager(item);
            if (queryID == 0) {
                insertDB(item);
            } else {
                updateDB(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void insertDB(EventItem item) {
        ItemDAO test = new ItemDAO(this);
        test.insert(item);
    }

    private void updateDB(EventItem item) {
        ItemDAO test = new ItemDAO(this);
        test.update(item);
    }

    private void prepareAlarmManager(EventItem item) {
        Intent intent = new Intent(this, PlayReceiver.class);
        Bundle bundle = new Bundle();
        Log.d(TAG, "prepareAlarmManager.." + item);
        bundle.putString("msg", "play_hskay");
        bundle.putString("audio_path", item.getAudioPath());
        bundle.putSerializable("event", item);
        intent.putExtras(bundle);

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, rightNow.getTimeInMillis(), pi);
    }

    AlertDialog mAlert;

    private void showDialogWithGridView() {
        Log.d(TAG, "showDialogWithGridView...");
        final ImageAdapter imageAdapter = new ImageAdapter(this);

        GridView gridview = new GridView(this);
        // gridview.setColumnWidth(90);
        gridview.setNumColumns(4);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Log.d(TAG, "onItemClick.........." + arg2);
                if (mIconChooser != null) {
                    Bitmap bmp = imageAdapter.getImagebyPosition(arg2);
                    mCurrentPhotoPath = imageAdapter.getImagePathbyPosition(arg2);
                    mIconChooser.setImageBitmap(bmp);
                }
                if (mAlert != null) {
                    mAlert.dismiss();
                }
            }
        });

        mAlert = new AlertDialog.Builder(this)
                .setTitle("Icon Chooser")
                .setView(gridview)
                .setPositiveButton(R.string.add_takepicture, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "play...........choose icon");
                        takePhotowithCamera();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "play...........cancel");
                    }
                }).create();
        // alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        mAlert.show();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Bitmap> mList = new ArrayList<Bitmap>();
        String[] files;

        public ImageAdapter(Context c) {
            mContext = c;
            AssetManager assetManager = c.getAssets();

            try {
                files = assetManager.list("");

                // List<String> it=Arrays.asList(files);
                Bitmap bitmap = null;
                for (int index = 0; index < files.length; index++) {
                    Log.d(TAG, "image path:" + files[index]);
                    InputStream is = assetManager.open(files[index]);
                    bitmap = BitmapFactory.decodeStream(is);
                    mList.add(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public String getImagePathbyPosition(int position) {

            if (position < files.length) {
                Log.d(TAG, "getImagePathbyPosition" + files[position]);
                return files[position];
            }
            Log.d(TAG, "getImagePathbyPosition null");
            return null;
        }

        public Bitmap getImagebyPosition(int position) {
            return (Bitmap) (mList != null ? mList.get(position) : 0);
        }

        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(mList.get(position));
            return imageView;
        }
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void takePhotowithCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            Log.d(TAG, "do piecture..");
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        Calendar calendar = Calendar.getInstance();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(calendar.getTime());
        String imageFileName = timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/record/");
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
                );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Show Image...");
            setPic();
            // mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mIconChooser.getWidth() != 0 ? mIconChooser.getWidth() : 60;
        int targetH = mIconChooser.getHeight() != 0 ? mIconChooser.getHeight() : 60;
        Log.d(TAG, "setPic." + targetW + " " + targetH + " mCurrentPhotoPath " + mCurrentPhotoPath);
        if (targetW == 0 || targetH == 0) {
            Log.d(TAG, "fail to set pcic");
            return;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        if (bitmap != null) {
            Log.d(TAG, "set bitmap");
        } else {
            Log.d(TAG, "set bitmap null");
        }
        mIconChooser.setImageBitmap(bitmap);
    }
}
