class UserAssignment < ActiveRecord::Base
  belongs_to :user
  belongs_to :assignment_description
  has_many :user_levels

  def self.find_or_create(userID, assignmentID)
    userAssignment = UserAssignment.find_by user_id: userID,
                                            assignment_description_id: assignmentID

    if userAssignment.nil?
      userAssignment = UserAssignment.create(user_id: userID,
                                             assignment_description_id: assignmentID,
                                             authToken: SecureRandom.uuid)
    end

    return userAssignment
  end

end
