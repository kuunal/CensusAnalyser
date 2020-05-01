 package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath,Class type, char seperator)  {
        checkType(IndiaCensusCSV.class,type);
        checkSeperator(seperator);
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            Iterator<IndiaCensusCSV> censusCSVIterator = getCSVFileIterator(reader, type ,seperator);
            return getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }catch (RuntimeException e){
            throw new CensusAnalyserException("Invalid Header",
                    CensusAnalyserException.ExceptionType.INVALID_HEADER);
        }
    }

    public int loadIndiaStateCode(String csvFilePath,Class type,char seperator)  {
        checkType(IndiaStateCode.class,type);
        checkSeperator(seperator);
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            Iterator<IndiaStateCode> stateCSVIterator = getCSVFileIterator(reader,type,seperator);
            return getCount(stateCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }catch (RuntimeException e){
            throw new CensusAnalyserException("Invalid Header",CensusAnalyserException.ExceptionType.INVALID_HEADER);
        }
    }

    public void checkSeperator(char seperator) throws CensusAnalyserException {
        if(seperator!=',')
            throw new CensusAnalyserException("Invalid seperator", CensusAnalyserException.ExceptionType.INVALID_SEPERATOR);
    }

    public void checkType(Class requiredType, Class providedType ) throws CensusAnalyserException {
        if(requiredType.equals(providedType)==false){
            throw new CensusAnalyserException("Invalid type",CensusAnalyserException.ExceptionType.INVALID_TYPE);
        }
    }

    public <E> Iterator<E> getCSVFileIterator(Reader reader,Class<E> className,char seperator)  {
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(className);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        csvToBeanBuilder.withSeparator(seperator);
        CsvToBean<E> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }

     <E> int getCount(Iterator<E> csvIterator){
        Iterable<E> csvIterable = () -> csvIterator;
        int numOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numOfEnteries;
    }
}
