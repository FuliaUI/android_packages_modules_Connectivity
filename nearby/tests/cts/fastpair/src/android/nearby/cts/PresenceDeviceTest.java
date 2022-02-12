/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.nearby.cts;

import static com.google.common.truth.Truth.assertThat;

import android.nearby.DataElement;
import android.nearby.NearbyDevice;
import android.nearby.PresenceDevice;
import android.os.Build;
import android.os.Parcel;

import androidx.annotation.RequiresApi;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Test for {@link PresenceDevice}.
 */
@RunWith(AndroidJUnit4.class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
public class PresenceDeviceTest {
    private static final int DEVICE_TYPE = PresenceDevice.DeviceType.PHONE;
    private static final String DEVICE_ID = "123";
    private static final String IMAGE_URL = "http://example.com/imageUrl";
    private static final int RSSI = -40;
    private static final int MEDIUM = NearbyDevice.Medium.BLE;
    private static final String DEVICE_NAME = "testDevice";
    private static final int KEY = 1234;
    private static final byte[] VALUE = new byte[]{1, 1, 1, 1};
    private static final byte[] SALT = new byte[]{2, 3};
    private static final byte[] SECRET_ID = new byte[]{11, 13};
    private static final byte[] ENCRYPTED_IDENTITY = new byte[]{1, 3, 5, 61};
    private static final long DISCOVERY_MILLIS = 100L;

    @Test
    public void testBuilder() {
        PresenceDevice device = new PresenceDevice.Builder()
                .setDeviceType(DEVICE_TYPE)
                .setDeviceId(DEVICE_ID)
                .setDeviceImageUrl(IMAGE_URL)
                .addExtendedProperty(new DataElement(KEY, VALUE))
                .setRssi(RSSI)
                .addMedium(MEDIUM)
                .setName(DEVICE_NAME)
                .setDiscoveryTimestampMillis(DISCOVERY_MILLIS)
                .setSalt(SALT)
                .setSecretId(SECRET_ID)
                .setEncryptedIdentity(ENCRYPTED_IDENTITY)
                .build();

        assertThat(device.getDeviceType()).isEqualTo(DEVICE_TYPE);
        assertThat(device.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(device.getDeviceImageUrl()).isEqualTo(IMAGE_URL);
        DataElement dataElement = device.getExtendedProperties().get(0);
        assertThat(dataElement.getKey()).isEqualTo(KEY);
        assertThat(Arrays.equals(dataElement.getValue(), VALUE)).isTrue();
        assertThat(device.getRssi()).isEqualTo(RSSI);
        assertThat(device.getMediums()).containsExactly(MEDIUM);
        assertThat(device.getName()).isEqualTo(DEVICE_NAME);
        assertThat(Arrays.equals(device.getSalt(), SALT)).isTrue();
        assertThat(Arrays.equals(device.getSecretId(), SECRET_ID)).isTrue();
        assertThat(Arrays.equals(device.getEncryptedIdentity(), ENCRYPTED_IDENTITY)).isTrue();
        assertThat(device.getDiscoveryTimestampMillis()).isEqualTo(DISCOVERY_MILLIS);
    }

    @Test
    public void testWriteParcel() {
        PresenceDevice device = new PresenceDevice.Builder()
                .setDeviceId(DEVICE_ID)
                .addExtendedProperty(new DataElement(KEY, VALUE))
                .setRssi(RSSI)
                .addMedium(MEDIUM)
                .setName(DEVICE_NAME)
                .build();

        Parcel parcel = Parcel.obtain();
        device.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        PresenceDevice parcelDevice = PresenceDevice.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertThat(parcelDevice.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(parcelDevice.getExtendedProperties().get(0).getKey()).isEqualTo(KEY);
        assertThat(parcelDevice.getRssi()).isEqualTo(RSSI);
        assertThat(parcelDevice.getMediums()).containsExactly(MEDIUM);
        assertThat(parcelDevice.getName()).isEqualTo(DEVICE_NAME);
    }
}
