package com.example.medicalarzi.util;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Field;

public class InstallPatientValidatorBlurListener implements BlurListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 5208361129473747962L;

	private Field<?> field;

    private String attribute;

	public InstallPatientValidatorBlurListener() {
		// TODO Auto-generated constructor stub
	}

	public InstallPatientValidatorBlurListener(Field<?> field, String attribute) {
		this.field = field;
		this.attribute = attribute;
	}

	@Override
	public void blur(BlurEvent event) {

		MedicalArziUtils.installSingleValidator(this.field, this.attribute);
	}

}
