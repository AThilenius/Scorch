class LevelDescription < ActiveRecord::Base
  belongs_to :assignment_description
  has_many :user_level
end
