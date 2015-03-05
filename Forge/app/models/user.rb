class User < ActiveRecord::Base
  has_many :user_assignments

  include SessionHelper

  def join_all(selectSql)
    return User.join_all(selectSql)
               .where("users.id = #{self.id}")
  end

  def self.join_all(selectSql)
    return User.select(selectSql)
               .joins('JOIN user_assignments ON user_assignments.user_id = users.id')
               .joins('JOIN assignment_descriptions ON user_assignments.assignment_description_id = assignment_descriptions.id')
               .joins('JOIN user_levels ON user_levels.user_assignment_id = user_assignments.id')
               .joins('JOIN level_descriptions ON user_levels.level_description_id = level_descriptions.id')
  end

end
