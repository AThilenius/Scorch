class AnvilController < ApplicationController
  def show
  end

  def list

  end

  def overview
  end

  # Partials
  def getting_started
    render partial: 'getting_started', :layout => false
  end

  def assignment_list
    return if sessionActiveCheckFailed

    @assignmentsData = []
    @assignments = AssignmentDescription.find_each

    # For each Assignment Description
    @assignments.each do |assignment|

      # TODO: These need to be collapsed into a single QUERY with JOINs
      # Get User Assignment
      userAssignment = UserAssignment.find_or_create(sessionGetUser.id, assignment.id)

      possiblePoints = LevelDescription.where(:assignment_description_id => assignment.id).sum(:points)
      earnedPoints = UserLevel.where(:user_assignment_id => userAssignment.id).sum(:points)

      possibleExtraCredit = LevelDescription.where(:assignment_description_id => assignment.id).sum(:extra_credit)
      earnedExtraCredit = UserLevel.where(:user_assignment_id => userAssignment.id).sum(:extra_credit)

      # Build out the 'assignment'
      assignmentData = OpenStruct.new
      assignmentData.assignment = assignment
      assignmentData.isOpen = assignment.open_date < Time.now.getutc
      assignmentData.userAssignment = userAssignment
      assignmentData.possiblePoints = possiblePoints
      assignmentData.earnedPoints = earnedPoints
      assignmentData.possibleExtraCredit = possibleExtraCredit
      assignmentData.earnedExtraCredit = earnedExtraCredit
      assignmentData.pointsPercent = possiblePoints == 0 ?
          '0%' : "#{(Float(earnedPoints) / Float(possiblePoints) * 100).round}%"
      assignmentData.extraCreditPercent = possibleExtraCredit == 0 ?
          '0%' : "#{(Float(earnedExtraCredit) / Float(possibleExtraCredit) * 100).round}%"
      assignmentData.dueDateColorLabel = earnedPoints == possiblePoints ? 'success' : assignment.due_date_color_label
      assignmentData.dueDateColor = earnedPoints == possiblePoints ? 'green' : assignment.due_date_color

      @assignmentsData << assignmentData
    end

    render partial: 'assignment_list', :layout => false
  end

  def assignment
    return if sessionActiveCheckFailed
    @assignment = AssignmentDescription.find(params[:id])
    render partial: 'assignment', :layout => false
  end

  def assignment_description
    return if sessionActiveCheckFailed

    @assignment = AssignmentDescription.find(params[:id])

    if @assignment.nil?
      flash[:error] = "Cannot find an assignment with ID: #{params[:id]}"
      redirect_to assignments_list_path
      return
    end

    @levelDescriptions = LevelDescription.where(:assignment_description_id => @assignment.id).order(levelNumber: :asc)

    # pull User Assignment and all User Levels
    @userAssignment = UserAssignment.find_or_create(sessionGetUser.id, @assignment.id)
    @userLevels = []
    @levels = []
    @levelDescriptions.each do |levelDescription|
      userLevel = UserLevel.find_or_create(@userAssignment.id, levelDescription.id)
      @userLevels << userLevel

      # Build out the 'level'
      level = OpenStruct.new
      level.levelDescription = levelDescription
      level.userLevel = userLevel

      percentage = 100
      if levelDescription.points != 0
        percentage = (100 * Float(userLevel.points) / Float(levelDescription.points)).round
      end

      case percentage
        when 0..59
          level.panelColor = 'danger'
        when 60..89
          level.panelColor = 'warning'
        else
          level.panelColor = 'success'
      end
      @levels << level

    end

    # Compute assignment level some statistics
    @totalPoints = @earnedPoints = 0
    @levelDescriptions.each do |level| @totalPoints += level.points end
    @userLevels.each do |userLevel| @earnedPoints += userLevel.points end
    @percentage = (100 * (Float(@earnedPoints) / Float(@totalPoints))).round

    case @percentage
      when 0..59
        @panelColor = 'danger'
      when 60..89
        @panelColor = 'warning'
      else
        @panelColor = 'success'
    end

    render partial: 'assignment_description', :layout => false
  end

  def assignment_progress
    return if sessionActiveCheckFailed

    @assignment = AssignmentDescription.find(params[:id])

    if @assignment.nil?
      flash[:error] = "Cannot find an assignment with ID: #{params[:id]}"
      redirect_to assignments_list_path
      return
    end

    @levelDescriptions = LevelDescription.where(:assignment_description_id => @assignment.id).order(levelNumber: :asc)
    @userAssignment = UserAssignment.find_or_create(sessionGetUser.id, @assignment.id)

    @graphData = []

    @levelDescriptions.each do |levelDescription|
      userLevel = UserLevel.find_or_create(@userAssignment.id, levelDescription.id)

      if userLevel.points > 0
        @graphData << {
            :value => userLevel.points,
            :color =>"#33CC33",
            :highlight => "#00FF00",
            :label => "Level #{levelDescription.levelNumber} - Completed"
        }
      end

      # Standard Level
      if levelDescription.extra_credit == 0 and levelDescription.points - userLevel.points > 0
        @graphData << {
            :value => levelDescription.points - userLevel.points,
            :color =>"#F7464A",
            :highlight => "#FF5A5E",
            :label => "Level: #{levelDescription.levelNumber} - Uncompleted"
        }
      end

      # Extra Credit Level
      if levelDescription.extra_credit != 0 and levelDescription.extra_credit - userLevel.points > 0
        @graphData << {
            :value => levelDescription.extra_credit - userLevel.points,
            :color =>"#3366FF",
            :highlight => "#6699FF",
            :label => "Extra Credit Level: #{levelDescription.levelNumber} - Uncompleted"
        }
      end

    end

    render partial: 'assignment_progress', :layout => false
  end

  def my_anvil
    render partial: 'my_anvil', :layout => false
  end

  def gs_osx
    render partial: 'gs_osx', :layout => false
  end

  def gs_win
    render partial: 'gs_win', :layout => false
  end

  def gs_other
    render partial: 'gs_other', :layout => false
  end

end