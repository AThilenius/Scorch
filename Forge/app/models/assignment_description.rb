class AssignmentDescription < ActiveRecord::Base

  # Formats a delta time to a nice, readable format with fixed precision
  def due_date_countdown()
    delta = Time.diff(dueDate, Time.now.getutc)
    prefix = open_date > Time.now.getutc ? 'Opens in ' : 'Due in '
    return 'Closed' if dueDate < Time.now.getutc
    if delta[:year] != 0
      return prefix << (delta[:year] == 1 ? '1 year' : "#{delta[:year]} years")
    elsif delta[:month] != 0
      return prefix << (delta[:month] == 1 ? '1 month' : "#{delta[:month]} months")
    elsif delta[:week] != 0
      return prefix << (delta[:week] == 1 ? '1 week' : "#{delta[:week]} weeks")
    elsif delta[:day] != 0
      return prefix << (delta[:day] == 1 ? '1 day' : "#{delta[:day]} days")
    elsif delta[:hour] != 0
      return prefix << (delta[:hour] == 1 ? '1 hour' : "#{delta[:hour]} hours")
    elsif delta[:minute] != 0
      return prefix << (delta[:minute] == 1 ? '1 minute' : "#{delta[:minute]} minutes")
    elsif delta[:second] != 0
      return prefix << (delta[:second] == 1 ? '1 second' : "#{delta[:second]} seconds")
    else
      return 'Error'
    end
  end

  def due_date_color()
    delta = dueDate - Time.now.getutc
    if delta <= 0 or open_date > Time.now.getutc
      return 'default'
    else
      return delta < 24 * 3600 ? 'danger' : 'warning'
    end
  end

  def is_past_due()
    return dueDate - Time.now.getutc <= 0
  end

end