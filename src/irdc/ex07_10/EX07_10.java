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
  /*�趨bIsReleasedһ��ʼΪfalse */
  private boolean bIsReleased = false;
  /*�趨bIsPausedһ��ʼΪfalse */
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
    
    /*ѡ���ļ�*/
    mFile.setOnClickListener(new ImageButton.OnClickListener(){
      public void onClick(View v)
      {
        Intent intent = new Intent(EX07_10.this,MyFileManager.class);  
        startActivityForResult(intent, FILE_RESULT_CODE);
      }
    });

    /* �����ֲ����ִ�е�Listener */  
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
    
    /*��ʼ��ť */
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
              /*�� MediaPlayer����*/
              myPlayer1.reset();
            }
            /*�趨 MediaPlayer��ȡSDcard�ĵ���*/
            myPlayer1.setDataSource( directory+"/"+filelist[curposi] );
            //System.out.println( directory+"/"+filelist[curposi]);
            myPlayer1.prepare();
            /*�� MediaPlayer��ʼ����*/
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
     
    /*��ͣ��ť */ 
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
                /*�趨 MediaPlayer��ͣ����*/
                myPlayer1.pause();
                bIsPaused = true;
                mTextView1.setText(R.string.str_pause);
                //mStart.setImageResource(R.drawable.stars);
                mPause.setImageResource(R.drawable.pause_2);
              }
              else if(bIsPaused==true) 
              { 
                /*�趨 MediaPlayer����*/
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
    
    /*����һ�׸�İ�ť */
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
              /*�� MediaPlayer����*/ 
              myPlayer1.reset();
            }
            /*�趨 MediaPlayer��ȡSDcard���ļ�*/
            myPlayer1.setDataSource( directory+"/"+filelist[curposi] );
            myPlayer1.prepare();
            /*����MediaPlayer*/
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
    
    /*����һ�׸谴ť */
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
              /*�� MediaPlayer����*/ 
              myPlayer1.reset();
            }
            /*�趨 MediaPlayer��ȡSDcard���ļ�*/
            myPlayer1.setDataSource( directory+"/"+filelist[curposi] );
            myPlayer1.prepare();
            /*�� MediaPlayer����*/
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
    
    /*ֹͣ��ť */
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
            /*�� MediaPlayer����*/
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
            mTextView1.setText("ѡ���ļ���Ϊ��"+bundle.getString("file"));
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
    /* ������ϵΪCHINESE*/
    Configuration conf = res.getConfiguration();
    conf.locale = Locale.CHINESE;
    DisplayMetrics dm = res.getDisplayMetrics();
    /* ������ϵ��� */
    res.updateConfiguration(conf, dm);
    
    super.onStart(); 
  }
}