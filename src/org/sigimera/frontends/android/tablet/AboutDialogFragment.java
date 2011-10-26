/**
 * Copyright (C) 2011 by Sigimera
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package org.sigimera.frontends.android.tablet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author Alex Oberhauser
 */
public class AboutDialogFragment extends DialogFragment {
	 
	public static AboutDialogFragment newInstance() {
         AboutDialogFragment frag = new AboutDialogFragment();
         return frag;
     }

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog dialog = new AlertDialog.Builder(getActivity()).setPositiveButton(android.R.string.ok,
                 	new DialogInterface.OnClickListener() {
             			public void onClick(DialogInterface dialog, int whichButton) {
             		}
         	}
         	).create();
         
         dialog.setTitle("About");
         
         WebView wv = new WebView(getActivity());
         wv.setBackgroundColor(Color.BLACK);
         
         StringBuffer strbuffer = new StringBuffer();
         strbuffer.append("<font color='white'>");
         strbuffer.append(this.getString(R.string.app_name) + " - v" 
                         + this.getString(R.string.version) + "<p/>");
         
         strbuffer.append("Sigimera is a Crisis Information Platform. The combination of information from different sources and the capability of social communication between the parties, makes the platform to a powerful instrument in and outside crisis areas.<p/>");
         
         strbuffer.append("&copy; 2011 by <a href='http://www.sigimera.org'>Sigimera</a>");
         strbuffer.append("</font>");
         
         wv.loadData(strbuffer.toString(), "text/html", "utf-8");

         dialog.setView(wv);
         return dialog;
     }

}
