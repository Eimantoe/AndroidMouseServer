package Utilities.Configuration;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager<TYPE> {

    private final Gson mGson = new Gson();
    private final String mPath;

    @SerializedName("Data")
    private List<TYPE> objectList = new ArrayList();
    
    public ConfigurationManager(String path) {

        this.mPath = path;
    }

    public boolean write(TYPE value)
    {
        return write(value, false);
    }

    public boolean write(TYPE value, boolean updateOnDuplicate)
    {
        boolean success = false;
        boolean written = false;

        FileReader fileReader;
        FileWriter fileWriter;

        try {
            fileReader = new FileReader(mPath);
            objectList = mGson.fromJson(fileReader, new TypeToken<List<TYPE>>(){}.getType());
            fileReader.close();

            if (objectList == null) {
                objectList = new ArrayList();
            }

            for (TYPE object : objectList) {
                if (object.equals(value)) {
                    if (updateOnDuplicate) {
                        objectList.remove(object);
                        objectList.add(value);
                    }
                    written = true;
                    break;
                }
            }

            if (!written) {
                objectList.add(value);
            }

            fileWriter = new FileWriter(mPath);
            mGson.toJson(objectList, fileWriter);

            fileWriter.close();

            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    public TYPE get(TYPE value)
    {
        FileReader fileReader;

        try {
            fileReader = new FileReader(mPath);
            objectList = mGson.fromJson(fileReader, new TypeToken<List<TYPE>>(){}.getType());
            fileReader.close();

            if (objectList == null) {
                return null;
            }

            for(TYPE object: objectList) {
                if (object.equals(value)) {
                    return object;
                }
            }

            return null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
