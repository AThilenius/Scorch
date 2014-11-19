module ApplicationHelper

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
    if page_title.empty?
      site_name
    else
      "#{page_title} | #{site_name}"
    end
  end

end