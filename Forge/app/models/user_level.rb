class UserLevel < ActiveRecord::Base
  belongs_to :user_assignment

  def self.find_or_create(userAssignmentID, levelDescriptionID)
    userLevel = UserLevel.find_by user_assignment_id: userAssignmentID,
                                  level_description_id: levelDescriptionID
    if userLevel.nil?
      userLevel = UserLevel.create(user_assignment_id: userAssignmentID,
                                   level_description_id: levelDescriptionID,
                                   points: 0)
    end

    return userLevel
  end

end