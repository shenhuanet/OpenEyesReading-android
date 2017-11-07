package com.shenhua.openeyesreading.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shenhua.openeyesreading.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shenhua on 6/12/2016.
 */
public class TextActivity extends AppCompatActivity {

    @Bind(R.id.tv_text)
    TextView tvText;
    private static final String CONTENT
            = "1、寄居在桂子山叔叔家中，不觉已是两个多月了，居室四周的环境极为情幽静谧，古朴自然之中又透着一种雅致。特别是那满山的木樨，中秋一过，便黄灿灿地开满枝头，悠悠的香气随风飘送。昨夜，西风渐紧，刮得楼下梧桐树“沙沙”地响，躺在床上，心想，明晨，又该是花落知多少了！今早推门一看，湿湿的水泥小径上，满是枯枝败叶，再细看枝叶间，黄灿灿的一片，竟是桂花！不由得脱口而出一句诗来：昨夜西风过园林，吹落黄花满地金！不禁心头一动，眼前幻化出父亲的影子来。";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        ButterKnife.bind(this);
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TextActivity.this, SinaPhotoActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                char[] cs = CONTENT.toCharArray();
                final StringBuffer buffer = new StringBuffer();
                for (char c : cs) {
                    buffer.append(c);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvText.setText(buffer.toString());
                        }
                    });
                }
            }
        }).start();
    }
}
