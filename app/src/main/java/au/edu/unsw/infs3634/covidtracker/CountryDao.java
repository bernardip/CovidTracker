package au.edu.unsw.infs3634.covidtracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDao {

    //Select all rows from country entity
    @Query("SELECT * FROM  country")
    List<Country> getCountries();

    //Select a country by countryCode
    @Query("SELECT * FROM country WHERE countryCode == :countryCode")
    Country getCountry(String countryCode);

    //Insert all
    @Insert
    void insertAll(Country... countries);

    //Delete all
    @Delete
    void deleteAll(Country... countries);

}
