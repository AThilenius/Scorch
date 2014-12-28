module ApplicationHelper

  include SessionHelper

  def markdown(text)
    options = {
        filter_html:      false,
        hard_wrap:        true,
        prettify:         true,
        link_attributes:  true
    }

    extensions = {
        autolink:           true,
        superscript:        true,
        fenced_code_blocks: true,
        underline:          true,
        highlight:          true,
        quote:              true,
        footnotes:          true,
        lax_spacing:        true,
        strikethrough:      true,
        tables:             true,
        link_attributes:    true

    }

    renderer = Redcarpet::Render::HTML.new(options)
    markdown = Redcarpet::Markdown.new(renderer, extensions)

    markdown.render(text).html_safe
  end

  def site_name
    "Forge"
  end

  def site_url
    if Rails.env.production?
      "http://www.Forge.com/"
    else
      # Our dev & test URL
      "http://localhost:3000"
    end
  end

  def meta_author
    "Alec Thilenius"
  end

  def meta_description
    "The web frontend for Scorch"
  end

  def meta_keywords
    ""
  end

  # Returns the full title on a per-page basis.
  # No need to change any of this we set page_title and site_name elsewhere.
  def full_title(page_title)
    if sessionIsLoggedIn
      "#{site_name} - #{sessionGetUser.lastName}"
    else
      site_name
    end
  end

  # attr_accessor for Redis
  def redis_accessor(prefix, *args)
    args.each do |arg|
      self.class_eval(%Q{
        def #{arg}
          return $redis.get("#{prefix}#{arg}")
        end
        })
      self.class_eval(%Q{
        def #{arg}=(val)
          $redis.set("#{prefix}#{arg}", val)
        end
        })
    end
  end

  def pending_assignments_count
    return 0 if sessionGetUser == nil

    count = 0
    nowUtc = Time.now.getutc.strftime('%Y-%m-%d %H:%M:%S')
    queryStr = "open_date < '#{nowUtc}' AND dueDate > '#{nowUtc}'"
    AssignmentDescription.where(queryStr).each do |assignment|
      openDate = assignment.open_date
      dueDate = assignment.dueDate
      now = Time.zone.now
      userAssignment = UserAssignment.find_or_create(sessionGetUser.id, assignment.id)
      possiblePoints = LevelDescription.where(:assignment_description_id => assignment.id).sum(:points)
      earnedPoints = UserLevel.where(:user_assignment_id => userAssignment.id).sum(:points)
      if possiblePoints != earnedPoints
        count += 1
      end
    end

    return count
  end

  def redis_getter(prefix, *args)
    args.each do |arg|
      self.class_eval(%Q{
        def #{arg}
          return $redis.get("#{prefix}_[#{@username}]_#{arg}")
        end
        })
    end
  end

    # attr_accessor for Redis
  def redis_setter(prefix, *args)
    args.each do |arg|
      self.class_eval(%Q{
        def #{arg}=(val)
          $redis.set("#{prefix}_[#{@username}]_#{arg}", val)
        end
        })
    end
  end

end