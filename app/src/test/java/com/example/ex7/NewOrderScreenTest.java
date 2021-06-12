package com.example.ex7;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadow.api.Shadow;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

@Config(manifest=Config.NONE)
@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner.class)
public class NewOrderScreenTest extends TestCase {

    @Test
    public void whenNewOrderScreenStarts_and_noOrder_than_customerNameIsEmpty()
    {
        NewOrderScreen newOrderScreen = Robolectric.buildActivity(NewOrderScreen.class).create().visible().get();
        EditText customer_name = newOrderScreen.findViewById(R.id.enter_name);
        assertEquals("",customer_name.getText().toString());
    }

    @Test
    public void whenNewOrderScreenStarts_HummusIsFalse()
    {
        NewOrderScreen newOrderScreen = Robolectric.buildActivity(NewOrderScreen.class).create().visible().get();
        Switch hummus = newOrderScreen.findViewById(R.id.hummus_switch);
        assertFalse(hummus.isChecked());

        }

    @Test
    public void whenNewOrderScreenStarts_TahiniIsFalse()
    {
        NewOrderScreen newOrderScreen = Robolectric.buildActivity(NewOrderScreen.class).create().visible().get();
        Switch tahini = newOrderScreen.findViewById(R.id.tahini_switch);
        assertFalse(tahini.isChecked());

    }

}