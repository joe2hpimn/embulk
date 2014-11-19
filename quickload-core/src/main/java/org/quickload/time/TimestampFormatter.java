package org.quickload.time;

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jruby.embed.ScriptingContainer;
import org.jruby.util.RubyDateFormat;
import static org.quickload.time.TimestampFormatConfig.parseDateTimeZone;
import org.quickload.spi.LineEncoder;
import org.quickload.config.ConfigException;

public class TimestampFormatter
{
    private final RubyDateFormat dateFormat;
    private final DateTimeZone timeZone;

    public TimestampFormatter(ScriptingContainer jruby, String format, TimestampFormatterTask task)
    {
        this.timeZone = parseDateTimeZone(task.getTimeZone());
        if (timeZone == null) {
            throw new ConfigException("Unsupported timezone '"+task.getTimeZone()+"'");
        }
        this.dateFormat = new RubyDateFormat(format, Locale.ENGLISH, true);
    }

    public void format(Timestamp value, LineEncoder encoder)
    {
        // TODO optimize by directly appending to internal buffer
        encoder.addText(format(value));
    }

    public String format(Timestamp value)
    {
        // TODO optimize by using reused StringBuilder
        dateFormat.setDateTime(new DateTime(value.toEpochMilli(), timeZone));
        dateFormat.setNSec(value.getNano());
        return dateFormat.format(null);
    }
}
