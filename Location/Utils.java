package com.devmaker.siftkin.extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DevmakerDiego on 04/12/2017.
 */

public class Utils {

    public static LatLng getLocalizacao(String endereco, Context context) throws Exception{
        int tentativas = 1;
        while (tentativas <= 5){
            LatLng latLng = getLocalizacaoTentativas(endereco,context);
            if(latLng != null)
                return latLng;
            Thread.sleep(700);
            tentativas++;
        }
        return new LatLng(0,0);

    }

    private static LatLng getLocalizacaoTentativas(String endereco, Context context){
        Geocoder geocoder =  new Geocoder(context);
        List<Address> address;
        LatLng localizacao = null;
        try {

            address = geocoder.getFromLocationName(endereco, 1);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            localizacao = new LatLng(location.getLatitude(), location.getLongitude() );

        }catch (Exception ex){
            Crashlytics.logException(ex);
            ex.printStackTrace();
        }

        return  localizacao;
    }

    public static Address getLocalizacao(Location location, Context context) throws Exception{
        int tentativas = 1;
        while (tentativas <= 5){
            Address addres = getLocalizacaoTentativas(location,context);
            if(addres != null)
                return addres;
            Thread.sleep(100);
            tentativas++;
        }
        return null;
    }

    private static Address getLocalizacaoTentativas(Location location, Context context){
        Geocoder geocoder =  new Geocoder(context);
        List<Address> address;
        LatLng localizacao = null;
        try {
            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            if (address == null) {
                return null;
            }

            Address addres = address.get(0);
            return  addres;
        }catch (Exception ex){
            Crashlytics.logException(ex);
            ex.printStackTrace();
        }
        return  null;
    }

    public static void resizeFile(File file, int requiredHeight) {
        try {
            String picturePath = file.getAbsolutePath();
            ExifInterface exifInterface = new ExifInterface(picturePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
            int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);

            if(width == 0 || height == 0){
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(picturePath), null, o);
                width = o.outWidth;
                height = o.outHeight;
            }

            int rotationDegrees = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationDegrees = 90;
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    rotationDegrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    rotationDegrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    rotationDegrees = 270;
                    break;
                default:
                    rotationDegrees = 0;
            }

            int scale = 1;
            while(width / scale / 2 >= requiredHeight &&
                    height / scale / 2 >= requiredHeight) {
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(picturePath), null, o2);

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotationDegrees);

            // Rotating Bitmap & convert to ARGB_8888, required by tess
            Bitmap image = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            FileOutputStream out = new FileOutputStream(picturePath);
            if(picturePath.endsWith(".png")){
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
            }else{
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    public static SimpleDateFormat getFormatDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    public static String formatDate(String data) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);
        return new SimpleDateFormat("yyyy-MM-dd").format(simpleDateFormat.parse(data));
    }

}