
case class TimeResponse(
                         abbreviation: String,
                         client_ip: String,
                         datetime: String,
                         day_of_week: Int,
                         day_of_year: Int,
                         dst: Boolean,
                         dst_from: Option[String],
                         dst_offset: Int,
                         dst_until: Option[String],
                         raw_offset: Int,
                         timezone: String,
                         unixtime: Int,
                         utc_datetime: String,
                         utc_offset: String,
                         week_number: Int)
