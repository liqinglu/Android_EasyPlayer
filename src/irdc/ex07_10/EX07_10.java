package irdc.ex07_10;

import java.io.IOException;
import android.app.Activity;
import android.app.ListActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout; 
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView; 
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.graphics.Color;

public class EX07_10 extends Activity
{
  public static final int FILE_RESULT_CODE = 1; 
  private ImageButton mPause, mNext, mBefore, mStart, mStop, mFile;
  private TextView mTextView1;
  private ListView mListView;
  //private ImageView mImageView1;
  /*设定bIsReleased一开始为false */
  private boolean bIsReleased = false;
  /*设定bIsPaused一开始为false */
  private boolean bIsPaused = false;
  public MediaPlayer myPlayer1 = new MediaPlayer();
  public String[] filelist = { "" };
  private String directory;
  private int curposi=0;
  private int listlength=0;
  private ArrayAdapter adapter;
  private AdapterView av;
  private View v;
  
  public void highLightChanged(int currentPosi, int step)
  {
    v = mListView.getChildAt(currentPosi);
    v.setBackgroundColor(Color.RED);
    
    if (currentPosi == 0 && step > 0)
    {
      v = mListView.getChildAt(listlength-1);
      v.setBackgroundColor(Color.TRANSPARENT);
    }
    else if ( currentPosi == listlength-1 && step < 0 )
    {
      v = mListView.getChildAt(0);
      v.setBackgroundColor(Color.TRANSPARENT);
    }
    else
    {
      v = mListView.getChildAt(currentPosi-step);
      v.setBackgroundColor(Color.TRANSPARENT);
    }
  }
  
  public void start()
  {
    try 
    { 
      if(myPlayer1.isPlaying()==true) 
      {
        myPlayer1.reset();
      }
      
      myPlayer1.setDataSource( directory+"/"+filelist[curposi] );

      myPlayer1.prepare();

      myPlayer1.start();

      mTextView1.setText( filelist[curposi]);
    }
    catch (IllegalStateException e) 
    { 
      // TODO Auto-generated catch block 
      mTextView1.setText(e.toString());
      e.printStackTrace();
    }
    catch (IOException e) 
    { 
      // TODO Auto-generated catch block 
      mTextView1.setText(e.toString());
      e.printStackTrace();
    }   
  }
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
     
    mStart = (ImageButton) findViewById(R.id.myImageButton1);
    mStop = (ImageButton) findViewById(R.id.myImageButton2);
    mPause = (ImageButton) findViewById(R.id.pause);
    mNext = (ImageButton) findViewById(R.id.next);
    mBefore = (ImageButton) findViewById(R.id.before);
    mFile = (ImageButton) findViewById(R.id.selectFile);
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    mListView = (ListView) findViewById(R.id.listview);
    
    adapter = new ArrayAdapter(this,R.layout.list_item,filelist);
    mListView.setAdapter(adapter);

    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent,View arg1,int position,long arg3){
        if( listlength>0 && position<=listlength ){
          curposi = position;
          for(int i=0;i<parent.getCount();i++){
                v=parent.getChildAt(i);
                if (position == i) {
                    v.setBackgroundColor(Color.RED);
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
          }
        }
      }
    });
    
    /*选择文件*/
    mFile.setOnClickListener(new ImageButton.OnClickListener(){
      public void onClick(View v)
      {
        Intent intent = new Intent(EX07_10.this,MyFileManager.class);  
        startActivityForResult(intent, FILE_RESULT_CODE);
      }
    });

    /* 当音乐播完会执行的Listener */  
    myPlayer1.setOnCompletionListener(new OnCompletionListener() 
    { 
      // @Override 
      public void onCompletion(MediaPlayer arg0) 
      {  
        myPlayer1.reset();
        mTextView1.setText(R.string.str_finished);
        mStart.setImageResource(R.drawable.stars);
        if ( curposi < listlength-1 )
        {
          curposi++;
        }
        else
        {
          curposi = 0;
        }
        
        highLightChanged(curposi,1);
        start();
      }
    });  
    
    /*开始按钮 */
    mStart.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        if ( listlength > 0 )
        {
          // TODO Auto-generated method stub 
          mStart.setImageResource(R.drawable.stars);
          //mImageView1.setImageResource(R.drawable.dance);
          mPause.setImageResource(R.drawable.pause);
          
          highLightChanged(curposi,1);
          
          try 
          { 
            if(myPlayer1.isPlaying()==true) 
            {
              /*把 MediaPlayer重设*/
              myPlayer1.reset();
            }
            /*设定 MediaPlayer读取SDcard的档案*/
            myPlayer1.setDataSource( directory+"/"+filelist[curposi] );
            //System.out.println( directory+"/"+filelist[curposi]);
            myPlayer1.prepare();
            /*把 MediaPlayer开始播放*/
            myPlayer1.start();
            //mTextView1.setText(R.string.str_start);
            mTextView1.setText( filelist[curposi]);
          }
          catch (IllegalStateException e) 
          { 
            // TODO Auto-generated catch block 
            mTextView1.setText(e.toString());
            e.printStackTrace();
          }
          catch (IOException e) 
          { 
            // TODO Auto-generated catch block 
            mTextView1.setText(e.toString());
            e.printStackTrace();
          }          
        }
      }
    });
     
    /*暂停按钮 */ 
    mPause.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      public void onClick(View view) 
      { 
        if( listlength > 0 )
        {
          if (myPlayer1 != null) 
          { 
            if(bIsReleased == false) 
            { 
              if(bIsPaused==false) 
              { 
                /*设定 MediaPlayer暂停播放*/
                myPlayer1.pause();
                bIsPaused = true;
                mTextView1.setText(R.string.str_pause);
                //mStart.setImageResource(R.drawable.stars);
                mPause.setImageResource(R.drawable.pause_2);
              }
              else if(bIsPaused==true) 
              { 
                /*设定 MediaPlayer播放*/
                myPlayer1.start();
                bIsPaused = false;
                mTextView1.setText(R.string.str_start);
                //mStart.setImageResource(R.drawable.stars);
                mPause.setImageResource(R.drawable.pause);
              }
            }
          }          
        }
      }
    });
    
    /*往下一首歌的按钮 */
    mNext.setOnClickListener(new ImageButton.OnClickListener() 
    {
      @Override
      public void onClick(View arg0)
      {
        if ( listlength > 0 )
        {
          // TODO Auto-generated method stub       
          mStart.setImageResource(R.drawable.stars);
          //mImageView1.setImageResource(R.drawable.dance);
          if ( curposi < listlength-1 )
          {
            curposi++;
          }
          else
          {
            curposi=0;
          }
          
          highLightChanged(curposi,1);
          
          try 
          {
            if(myPlayer1.isPlaying()==true) 
            { 
              /*把 MediaPlayer重设*/ 
              myPlayer1.reset();
            }
            /*设定 MediaPlayer读取SDcard的文件*/
            myPlayer1.setDataSource( directory+"/"+filelist[curposi] );
            myPlayer1.prepare();
            /*启动MediaPlayer*/
            myPlayer1.start();
            mTextView1.setText( filelist[curposi] );
          }
          catch (IllegalStateException e) 
          {
            // TODO Auto-generated catch block 
            mTextView1.setText(e.toString());
            e.printStackTrace();
          }
          catch (IOException e) 
          { 
            // TODO Auto-generated catch block 
            mTextView1.setText(e.toString());
            e.printStackTrace();
          }          
        }
      }
    });
    
    /*往上一首歌按钮 */
    mBefore.setOnClickListener(new ImageButton.OnClickListener() 
    {
      @Override
      public void onClick(View arg0)
      {
        if ( listlength > 0 )
        {
          // TODO Auto-generated method stub
          mStart.setImageResource(R.drawable.stars);
          //mImageView1.setImageResource(R.drawable.dance);
          if ( curposi > 0 )
          {
            curposi--;
          }
          else
          {
            curposi= listlength-1;
          }
          
          highLightChanged(curposi,-1);
          
          try 
          { 
            if(myPlayer1.isPlaying()==true) 
            { 
              /*将 MediaPlayer重设*/ 
              myPlayer1.reset();
            }
            /*设定 MediaPlayer读取SDcard的文件*/
            myPlayer1.setDataSource( directory+"/"+filelist[curposi] );
            myPlayer1.prepare();
            /*把 MediaPlayer启动*/
            myPlayer1.start();
            mTextView1.setText(filelist[curposi]);
          }
          catch (IllegalStateException e) 
          { 
            // TODO Auto-generated catch block 
            mTextView1.setText(e.toString());
            e.printStackTrace();
          }
          catch (IOException e) 
          { 
            // TODO Auto-generated catch block 
            mTextView1.setText(e.toString());
            e.printStackTrace();
          }          
        }
      }
    });
    
    /*停止按钮 */
    mStop.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        if (listlength > 0)
        {
          // TODO Auto-generated method stub 
          
          if(myPlayer1.isPlaying()==true) 
          { 
            /*将 MediaPlayer重设*/
            myPlayer1.reset();
            mTextView1.setText(R.string.str_stopped);
            mStart.setImageResource(R.drawable.stars);
            mPause.setImageResource(R.drawable.pause);
            //mImageView1.setImageResource(R.drawable.black);
          }
          else
          {
            myPlayer1.reset();
          }          
        }
      }
    });
  }

  @Override  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
      if(FILE_RESULT_CODE == requestCode){  
          Bundle bundle = null;  
          if(data!=null&&(bundle=data.getExtras())!=null){  
            mTextView1.setText("选择文件夹为："+bundle.getString("file"));
            directory = bundle.getString("file");
            filelist = bundle.getStringArray("music");
            listlength = filelist.length;
            //adapter = (ArrayAdapter)mListView.getAdapter();
            //adapter.notifyDataSetChanged();
            adapter = new ArrayAdapter(this,R.layout.list_item,filelist);
            mListView.setAdapter(adapter);
          }  
      }  
  } 

  @Override
  protected void onStart()
  {
    Resources res = getResources();
    /* 更改语系为CHINESE*/
    Configuration conf = res.getConfiguration();
    conf.locale = Locale.CHINESE;
    DisplayMetrics dm = res.getDisplayMetrics();
    /* 保存语系变更 */
    res.updateConfiguration(conf, dm);
    
    super.onStart(); 
  }
}