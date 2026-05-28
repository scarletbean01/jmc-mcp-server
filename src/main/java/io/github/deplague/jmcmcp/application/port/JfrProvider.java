package io.github.deplague.jmcmcp.application.port;

import java.io.IOException;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Port for loading and filtering JFR recordings.
 * Implemented by infrastructure adapters.
 */
public interface JfrProvider {

    /**
     * Load a JFR recording from the given file path.
     *
     * @param filePath path to the .jfr file
     * @return the loaded item collection
     * @throws IOException if the file cannot be read or parsed
     */
    IItemCollection loadRecording(String filePath) throws IOException;

    /**
     * Filter the event collection by an optional time range.
     *
     * @param events       the events to filter
     * @param startTimeStr optional ISO-8601 start time
     * @param endTimeStr   optional ISO-8601 end time
     * @return filtered events
     */
    IItemCollection filterByTimeRange(
            IItemCollection events,
            String startTimeStr,
            String endTimeStr
    );
}
