package frc3824.databaserelay;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import frc3824.databaserelay.Comms.DataMessage;

/**
 * @author frc3824
 * Created: 3/26/17
 */

public class NumberButton extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private TextView mTextView;
    private EditText mEditText;
    private ImageButton mDecrement;
    private ImageButton mIncrement;
    private int mNumber;
    private String mKey;

    public NumberButton(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.number_button, this);

        mTextView  = (TextView)    findViewById(R.id.label);
        mDecrement = (ImageButton) findViewById(R.id.decrement);
        mIncrement = (ImageButton) findViewById(R.id.increment);
        mEditText  = (EditText)    findViewById(R.id.number);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberButton);
        mTextView.setText(typedArray.getString(R.styleable.NumberButton_label));
        mKey = typedArray.getString(R.styleable.NumberButton_key);
        typedArray.recycle();



        mNumber = 0;
        mEditText.setText(String.valueOf(mNumber));
        mEditText.addTextChangedListener(this);

        mDecrement.setOnClickListener(this);
        mIncrement.setOnClickListener(this);
        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.decrement:
                // Can't go negative
                if(mNumber > 0) {
                    mNumber--;
                }
                break;
            case R.id.increment:
                mNumber ++;
                break;
            case R.id.label:
                JSONObject o = new JSONObject();
                try {
                    o.put(mKey, mNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    AppContext.getServerConnection().send(new DataMessage("update", o));
                }
                break;
        }

        mEditText.setText(String.valueOf(mNumber));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int temp = 0;
        try {
            temp = Integer.parseInt(s.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            mNumber = temp;
        }
    }
}
