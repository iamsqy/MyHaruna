/*
MyHaruna - Display a Yuru-chara
Copyright (C) 2021  iamsqy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package com.iamsqy.myharuna;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TestService extends Service {
    boolean timeFlag = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createWindowView();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    void speak(int index) {
        if (timeFlag == false) {
            String list[] = {",,Ծ‸Ծ,,", "(╯‵□′)╯︵┻━┻", "=‿=✧", "●ω●", "(/ ▽ \\)", "(=・ω・=)", "(●'◡'●)ﾉ", "<(▰˘◡˘▰)>", "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(ง,,• ᴗ •,,)ง ✧", ">ㅂ<ﾉ ☆", "Σ( ° △ °|||)︴", "┌( ಠ_ಠ)┘", "(ﾟДﾟ≡ﾟдﾟ)!?", "∑(っ °Д °;)っ", "＞︿＜", "＞△＜", "●︿●", "(´；ω；`)", "◐▽◑", "ʅ（´◔౪◔）ʃ", "_(:3 」∠)_", "_(┐「ε:)_", "(°▽°)ﾉ", "←◡←", "_(•̀ᴗ•́ 」∠ ❀)_", "_φ(･ω･` )", "噫", "哧溜~", "绅士是对我的夸奖"};
            Toast.makeText(TestService.this, list[index], Toast.LENGTH_SHORT).show();
            timeFlag = true;
        }
        return;
    }

    private void createWindowView() {
        final WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        /*
        settings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
         */
        webView.loadUrl("file:///android_asset/test.html");
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.alpha = 0.67f;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 150;
        params.y = 150;
        params.width = 700;
        params.height = 900;
        webView.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Random r = new Random();
                Timer timer = new Timer();
                timer.schedule(new MyTask(), 10000);
                int rand = r.nextInt(29);
                speak(rand);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        wm.updateViewLayout(webView, params);
                        break;
                }
                return true;
            }
        });
        wm.addView(webView, params);
    }
    class MyTask extends TimerTask {
        public void run() {
            timeFlag = false;
        }
    }
}
