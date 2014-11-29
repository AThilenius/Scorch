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
        tables:             true

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