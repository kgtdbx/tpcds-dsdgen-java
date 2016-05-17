/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teradata.tpcds;

import io.airlift.airline.Option;

import java.util.Optional;

public class Options
{
    @Option(name = {"--scale", "-s"}, title = "scale", description = "Volume of data to generate in GB (Default: 1)")
    public int scale = 1;

    @Option(name = {"--directory", "-d"}, title = "directory", description = "Directory to put generated files (Default: .) ")
    public String directory = ".";

    @Option(name = "--suffix", title = "suffix", description = "Suffix for generated data files (Default: .dat)")
    public String suffix = ".dat";

    @Option(name = {"--table", "-t"}, title = "table", description = "Build only the specified table.  If this property is not specified, all tables will be generated")
    public String table = null;

    @Option(name = {"--null"}, title = "null", description = "String representation for null values (Default: the empty string)")
    public String nullString = "";

    @Option(name = {"--separator"}, title = "separator", description = "Separator between columns (Default: |)")
    public char separator = '|';

    @Option(name = {"--terminate"}, title = "terminate", description = "Terminate each row with a separator (Default: true)")
    public boolean terminate = true;

    public Session toSession()
    {
        validateProperties();
        return new Session(scale,
                           directory,
                           suffix,
                           toTableOptional(table),
                           nullString,
                           separator,
                           terminate);
    }

    private static Optional<Table> toTableOptional(String table)
    {
        if (table == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Table.valueOf(table.toUpperCase()));
        }
        catch (IllegalArgumentException e) {
            throw new InvalidOptionException("table", table);
        }
    }

    private void validateProperties()
    {
        if (scale < 0 || scale > 100000) {
            throw new InvalidOptionException("scale", Integer.toString(scale), "Scale must be greater than 0 and less than 100000");
        }
        if (directory.equals("")) {
            throw new InvalidOptionException("directory", directory, "Directory cannot be an empty string");
        }
        if (suffix.equals("")) {
            throw new InvalidOptionException("suffix", suffix, "Suffix cannot be an empty string");
        }
    }
}
