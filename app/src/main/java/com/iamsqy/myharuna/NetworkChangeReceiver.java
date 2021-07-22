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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {
    boolean flag = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (flag) {
            Toast.makeText(context, R.string.Haruna_network_change_detected, Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton(R.string.dig_a_hole, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setMessage(R.string.Haruna_network_change_confirm)
                    .setTitle(R.string.Haruna_network_change_detected);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        flag = true;
    }
}
