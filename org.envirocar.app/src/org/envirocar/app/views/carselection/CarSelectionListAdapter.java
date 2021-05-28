/**
 * Copyright (C) 2013 - 2019 the enviroCar community
 *
 * This file is part of the enviroCar app.
 *
 * The enviroCar app is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The enviroCar app is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with the enviroCar app. If not, see http://www.gnu.org/licenses/.
 */
package org.envirocar.app.views.carselection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.card.MaterialCardView;

import org.envirocar.app.R;
import org.envirocar.core.entity.Car;
import org.envirocar.core.logging.Logger;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author dewall
 */
public class CarSelectionListAdapter extends RecyclerView.Adapter<CarSelectionListAdapter.CarViewHolder>{
    private static final Logger LOG = Logger.getLogger(CarSelectionListAdapter.class);

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_car_selection_layout_carlist_entry, parent, false);
        return new CarViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        final Car car = mCars.get(position);
        // set the views
        holder.firstLine.setText(String.format("%s - %s", car.getManufacturer(), car.getModel()));
        holder.

        // If this car is the selected car, then set the radio button checked.
        if (mSelectedCar != null && mSelectedCar.equals(car)) {
            mSelectedButton = holder.mRadioButton;
            mSelectedButton.setChecked(true);
        }

        final CarViewHolder tmpHolder = holder;
        // set the onClickListener of the radio button.
        holder.mRadioButton.setOnClickListener(v -> {
            if (mSelectedCar == null) {
                mSelectedCar = car;
                mSelectedButton = tmpHolder.mRadioButton;
            } else if (!mSelectedCar.equals(car)) {
                mSelectedCar = car;
                if (mSelectedButton != null)
                    mSelectedButton.setChecked(false);
                mSelectedButton = tmpHolder.mRadioButton;
            }
            tmpHolder.mRadioButton.setChecked(true);
            mCallback.onSelectCar(mSelectedCar);
        });

        // Set the onClickListener for a single row.
        holder.cardView.setOnClickListener(v -> new MaterialDialog.Builder(mContext)
                .items(R.array.car_list_option_items)
                .itemsCallback((materialDialog, view, i, charSequence) -> {
                    switch (i) {
                        case 0:
                            if(car.equals(mSelectedCar))
                                return;

                            // Uncheck the currently checked car.
                            if (mSelectedButton != null) {
                                mSelectedButton.setChecked(false);
                            }

                            // Set the new car as selected car type.
                            mSelectedCar = car;
                            mSelectedButton = tmpHolder.mRadioButton;
                            mSelectedButton.setChecked(true);

                            // Call the callback in order to react accordingly.
                            mCallback.onSelectCar(car);
                            break;
                        case 1:
                            // Uncheck the the previously checked radio button and update the
                            // references accordingly.
                            if (car.equals(mSelectedCar)) {
                                mSelectedCar = null;
                                mSelectedButton.setChecked(false);
                                mSelectedButton = null;
                            }

                            // Call the callback
                            mCallback.onDeleteCar(car);
                            break;
                        default:
                            LOG.warn("No action selected!");
                    }
                })
                .show());
    }

    @Override
    public int getItemCount(){
        return mCars.size();
    }

    /**
     * Simple callback interface for the action types of the car list entries.
     */
    public interface OnCarListActionCallback {
        /**
         * Called whenever a car has been selected to be the used car.
         *
         * @param car the selected car
         */
        void onSelectCar(Car car);

        /**
         * Called whenever a car should be deleted.
         *
         * @param car the selected car.
         */
        void onDeleteCar(Car car);
    }

    /**
     * Context of the current scope.
     */
    private final Context mContext;

    /**
     * Callback
     */
    private final OnCarListActionCallback mCallback;

    private Car mSelectedCar;
    private RadioButton mSelectedButton;
    private final List<Car> mCars;

    /**
     * Constructor.
     *
     * @param context     the context of the current scope.
     * @param selectedCar the car for which the radio button gets checked.
     * @param values      the values to show in the list.
     * @param callback    the callback for list actions
     */
    public CarSelectionListAdapter(Context context, Car selectedCar, List<Car> values,
                                   OnCarListActionCallback callback) {
        this.mContext = context;
        this.mCars = values;
        this.mCallback = callback;
        this.mSelectedCar = selectedCar;
    }

    /**
     * Adds a new {@link Car} to the list and finally invalidates the lsit.
     *
     * @param car the car to add to the list
     */
    protected void addCarItem(Car car) {
        this.mCars.add(car);
        notifyDataSetChanged();
    }

    /**
     * Removes a {@link Car} from the list and finally invalidates the list.
     *
     * @param car the car to remove from the list.
     */
    protected void removeCarItem(Car car) {
        if (mCars.contains(car)) {
            mCars.remove(car);
            notifyDataSetChanged();
        }
    }

    /**
     * Static view holder class that holds all necessary views of a list-row.
     */
    static class CarViewHolder extends RecyclerView.ViewHolder {

        protected final View mCoreView;

        @BindView(R.id.Imageview)
        protected ImageView iconView;
        @BindView(R.id.textviewww)
        protected TextView firstLine;
        @BindView(R.id.activity_car_selection_layout_carlist_entry_radio)
        protected RadioButton mRadioButton;
        @BindView(R.id.activity_car_selection_layout_carlist_entry_year_layout)
        protected TextView year;
        @BindView(R.id.car_select_layout)
        protected MaterialCardView cardView;
        @BindView(R.id.activity_car_selection_layout_carlist_entry_gasoline)
        protected TextView fuelType;
        @BindView(R.id.activity_car_selection_layout_carlist_entry_engine)
        protected TextView engineDisplacement;




        /**
         * Constructor.
         *
         * @param view
         */
        CarViewHolder(View view) {
            super(view);
            this.mCoreView = view;
            ButterKnife.bind(this, view);
        }
    }
}