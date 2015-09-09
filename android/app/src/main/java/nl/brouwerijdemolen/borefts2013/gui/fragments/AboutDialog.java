package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;

@EFragment(R.layout.dialog_about)
public class AboutDialog extends DialogFragment {

	@ViewById
	protected TextView logoText, licensesText;

	public AboutDialog() {
		setRetainInstance(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@AfterViews
	protected void init() {
		logoText.setText(MolenTypefaceSpan.makeMolenSpannable(getActivity(), getString(R.string.app_name_short)));
		licensesText.setText(Html.fromHtml(getString(R.string.about_licenses)));
	}

	@Click(R.id.visit2312_button)
	protected void visit2312Clicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://2312.nl")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Click(R.id.visitdemolen_button)
	protected void visitdemolenClicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.brouwerijdemolen.nl")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}
