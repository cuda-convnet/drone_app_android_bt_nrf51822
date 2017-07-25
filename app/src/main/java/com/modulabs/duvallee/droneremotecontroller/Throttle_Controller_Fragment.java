package com.modulabs.duvallee.droneremotecontroller;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by duval on 2017-07-03.
 */

public class Throttle_Controller_Fragment extends Fragment implements View.OnClickListener
{
    // --------------------------------------------------------------------------------
    // main activity
    private MainRemoteControllerActivity mParrent;

    // --------------------------------------------------------------------------------
    // range seekbar
    private RangeSeekBar<Integer> m_arming_seekbar_test = null;
    private RangeSeekBar<Integer> m_throttle_seekbar_test = null;

    // --------------------------------------------------------------------------------
    private final int DEFAULT_GEAR_VALUE = 0;
    private final int DEFAULT_ARMING_VALUE = 512;
    private final int DEFAULT_THROTTLE_VALUE = 0;

    private final int THROTTLE_SPEED_MAX_LEVEL = 6;
    private final int THROTTLE_SPEED_LEVEL_VALUE = 5;

    private int m_throttle_speed_level = 0;
    private int m_throttle_level_unit = 1;

    private int m_throttle_value = DEFAULT_THROTTLE_VALUE;

    private TextView mLevel_TextView = null;

    // ****************************************************************************************** //
    //
    // constructor
    //
    //
    // ****************************************************************************************** //
    public Throttle_Controller_Fragment(MainRemoteControllerActivity p)
    {
        // Required empty public constructor }
        mParrent = p;
    }

    // ****************************************************************************************** //
    //
    // void onCreate(Bundle savedInstanceState)
    //
    //
    // ****************************************************************************************** //
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    // ****************************************************************************************** //
    //
    // public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    //
    //
    // ****************************************************************************************** //
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout linearlayout = new LinearLayout(getActivity());
        View view = inflater.inflate(R.layout.throttle_fragment, container, false);

        // -----------------------------------------------------------------------------------------
        ImageButton back_button = (ImageButton) view.findViewById(R.id.backButton);
        back_button.setOnClickListener(this);
        back_button.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                ImageButton backButton = (ImageButton) v.findViewById(R.id.backButton);

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    backButton.setImageResource(R.mipmap.select_prev);
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    backButton.setImageResource(R.mipmap.unselect_prev);
                    mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                    return true;
                }
                return true;
            }
        });
        
        ImageButton set_default_value_Button = (ImageButton) view.findViewById(R.id.defaultButton);
        set_default_value_Button.setOnClickListener(this);
        set_default_value_Button.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();
                UartService uartservice = mParrent.getUartService();
                ImageButton set_default_value_Button = (ImageButton) v.findViewById(R.id.defaultButton);

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    set_default_value_Button.setImageResource(R.mipmap.select_default_button);
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    set_default_value_Button.setImageResource(R.mipmap.unselect_default_button);
                    if (uartservice == null)
                    {
                        Toast.makeText(mParrent, "Not connected the Drone BT Transmitter", Toast.LENGTH_LONG).show();
                        mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                        return true;
                    }

                    m_throttle_value = DEFAULT_THROTTLE_VALUE;
                    droneProtocol.set_throttle_value(m_throttle_value);
                    droneProtocol.set_gear_value(DEFAULT_GEAR_VALUE);
                    if (droneProtocol.Send_Channel_Message(uartservice) < 0)
                    {
                        Toast.makeText(mParrent, "Busy state !!!", Toast.LENGTH_SHORT).show();
                    }

                    m_arming_seekbar_test.setValues(DEFAULT_GEAR_VALUE);
                    m_throttle_seekbar_test.setValues(m_throttle_value);
                    return true;
                }
                return false;
            }
        });

        ImageButton throttle_down_button = (ImageButton) view.findViewById(R.id.throttle_down_Button);
        throttle_down_button.setOnClickListener(this);
        throttle_down_button.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();
                UartService uartservice = mParrent.getUartService();
                ImageButton throttle_down_button = (ImageButton) v.findViewById(R.id.throttle_down_Button);

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    throttle_down_button.setImageResource(R.mipmap.select_left);
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    throttle_down_button.setImageResource(R.mipmap.unselect_left);
                    if (uartservice == null)
                    {
                        Toast.makeText(mParrent, "Not connected the Drone BT Transmitter", Toast.LENGTH_LONG).show();
                        mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                        return true;
                    }

                    if ((m_throttle_value - m_throttle_level_unit) <= droneProtocol.get_throttle_min_value())
                    {
                        m_throttle_value = droneProtocol.get_throttle_min_value();
                    }
                    else
                    {
                        m_throttle_value -= m_throttle_level_unit;
                    }

                    droneProtocol.set_throttle_value(m_throttle_value);
                    m_throttle_seekbar_test.setValues(m_throttle_value);

                    if (droneProtocol.Send_Channel_Message(uartservice) < 0)
                    {
                        Toast.makeText(mParrent, "Busy state !!!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        ImageButton throttle_level_button = (ImageButton) view.findViewById(R.id.throttle_level_Button);
        throttle_level_button.setOnClickListener(this);

        ImageButton throttle_up_button = (ImageButton) view.findViewById(R.id.throttle_up_Button);
        throttle_up_button.setOnClickListener(this);
        throttle_up_button.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();
                UartService uartservice = mParrent.getUartService();
                ImageButton throttle_up_button = (ImageButton) v.findViewById(R.id.throttle_up_Button);

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    throttle_up_button.setImageResource(R.mipmap.select_right);
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    throttle_up_button.setImageResource(R.mipmap.unselect_right);
                    if (uartservice == null)
                    {
                        Toast.makeText(mParrent, "Not connected the Drone BT Transmitter", Toast.LENGTH_LONG).show();
                        mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                        return true;
                    }

                    if ((m_throttle_value + m_throttle_level_unit) >= droneProtocol.get_throttle_max_value())
                    {
                        m_throttle_value = droneProtocol.get_throttle_max_value();
                    }
                    else
                    {
                        m_throttle_value += m_throttle_level_unit;
                    }

                    droneProtocol.set_throttle_value(m_throttle_value);
                    m_throttle_seekbar_test.setValues(m_throttle_value);

                    if (droneProtocol.Send_Channel_Message(uartservice) < 0)
                    {
                        Toast.makeText(mParrent, "Busy state !!!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        // -----------------------------------------------------------------------------------------
        Switch arming_switch = (Switch) view.findViewById(R.id.armingSwitchButton);
        arming_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    buttonView.setText("DISARM");
                }
                else
                {
                    buttonView.setText("ARM");
                }
                DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();
                UartService uartservice = mParrent.getUartService();

                if (uartservice == null)
                {
                    Toast.makeText(mParrent, "Not connected the Drone BT Transmitter", Toast.LENGTH_LONG).show();
                    mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                    return;
                }

                if (isChecked)
                {
                    droneProtocol.set_gear_value(DEFAULT_ARMING_VALUE);
                    m_arming_seekbar_test.setValues(DEFAULT_ARMING_VALUE);
                }
                else
                {
                    droneProtocol.set_gear_value(DEFAULT_GEAR_VALUE);
                    m_arming_seekbar_test.setValues(DEFAULT_GEAR_VALUE);
                }

                if (droneProtocol.Send_Channel_Message(uartservice) < 0)
                {
                    Toast.makeText(mParrent, "Busy state !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLevel_TextView = (TextView) view.findViewById(R.id.textLevelThrottle);

        // -----------------------------------------------------------------------------------------
        // arming seekbar
        m_arming_seekbar_test = new RangeSeekBar<Integer>(mParrent, true, true);

        m_arming_seekbar_test.setValueLabel("arm");

        m_arming_seekbar_test.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>()
        {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue, Integer Value)
            {
                DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();
                UartService uartservice = mParrent.getUartService();

                if (uartservice == null)
                {
                    Toast.makeText(mParrent, "Not connected the Drone BT Transmitter", Toast.LENGTH_LONG).show();
                    mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                    return;
                }
                droneProtocol.set_gear_value(Value);

                if (droneProtocol.Send_Channel_Message(uartservice) < 0)
                {
                    Toast.makeText(mParrent, "Busy state !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // -----------------------------------------------------------------------------------------
        // layout of throttle
        LinearLayout arming_Layout = (LinearLayout) view.findViewById(R.id.test_arming_linear_layout);
        arming_Layout.addView(m_arming_seekbar_test);

        DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();

        int gear_min = droneProtocol.get_gear_min_value();
        int gear_value = droneProtocol.get_gear_value();
        int gear_max = droneProtocol.get_gear_max_value();

        m_arming_seekbar_test.setRangeValues(droneProtocol.get_absolute_min_value(), droneProtocol.get_absolute_max_value());
        m_arming_seekbar_test.setSelectedMinValue(gear_min);
        m_arming_seekbar_test.setSelectedMaxValue(gear_max);
        m_arming_seekbar_test.setValues(gear_value);

        // -----------------------------------------------------------------------------------------
        // throttle
        m_throttle_seekbar_test = new RangeSeekBar<Integer>(mParrent, true, true);
        m_throttle_seekbar_test.setValueLabel("throttle");
//        m_throttle_seekbar_test.setNotifyWhileDragging(true);

        m_throttle_seekbar_test.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>()
        {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue, Integer Value)
            {
                DroneRemoteControllerProtocol droneProtocol = mParrent.getProtocol();
                UartService uartservice = mParrent.getUartService();

                if (uartservice == null)
                {
                    Toast.makeText(mParrent, "Not connected the Drone BT Transmitter", Toast.LENGTH_LONG).show();
                    mParrent.switch_view(mParrent.VIEW_MAIN_MENU_SCREEN_INDEX);
                    return;
                }
                m_throttle_value = Value;
                droneProtocol.set_throttle_value(m_throttle_value);

                if (droneProtocol.Send_Channel_Message(uartservice) < 0)
                {
                    Toast.makeText(mParrent, "Busy state !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // layout of throttle
        LinearLayout throttle_Layout = (LinearLayout) view.findViewById(R.id.test_throttle_linear_layout);
        throttle_Layout.addView(m_throttle_seekbar_test);

        int throttle_min = droneProtocol.get_throttle_min_value();
        m_throttle_value = droneProtocol.get_throttle_value();
        int throttle_max = droneProtocol.get_throttle_max_value();

        m_throttle_seekbar_test.setRangeValues(droneProtocol.get_absolute_min_value(), droneProtocol.get_absolute_max_value());
        m_throttle_seekbar_test.setSelectedMinValue(throttle_min);
        m_throttle_seekbar_test.setSelectedMaxValue(throttle_max);
//        m_throttle_seekbar_test.setValues(m_throttle_value);
        m_throttle_seekbar_test.setSelectedValue(m_throttle_value);

        // -----------------------------------------------------------------------------------------
        linearlayout.addView(view);

        return linearlayout;
    }


    // ****************************************************************************************** //
    //
    // void onClick(final View v)
    //
    //
    // ****************************************************************************************** //
    public void onClick(final View v)
    {
        switch (v.getId())
        {
            case R.id.throttle_level_Button :
                m_throttle_speed_level += 1;
                m_throttle_speed_level %= THROTTLE_SPEED_MAX_LEVEL;

                if (m_throttle_speed_level == 0)
                {
                    m_throttle_level_unit = 1;
                }
                else
                {
                    m_throttle_level_unit = (m_throttle_speed_level * THROTTLE_SPEED_LEVEL_VALUE);
                }
                if (mLevel_TextView != null)
                {
                    mLevel_TextView.setText(String.valueOf(m_throttle_level_unit));
                }
                break;
        }
    }
}
