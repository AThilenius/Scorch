class AssignmentsController < ApplicationController

  include SessionHelper

  def list
    return if sessionActiveCheckFailed
  end

  def show
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

      case (100 * Float(userLevel.points) / Float(levelDescription.points)).round
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

  end

  def destroy
    return if sessionAdminCheckFailed

    @assignment = AssignmentDescription.find(params[:id])
    if @assignment.nil?
      flash[:error] = "Cannot find an assignment with ID: #{params[:id]}"
    else
      flash[:notice] = "Assignment #{@assignment.name} deleted successfully."
      @assignment.delete
    end

    redirect_to assignments_list_path
  end

  def new
    return if sessionAdminCheckFailed
  end

  def create

    return if sessionAdminCheckFailed
    didPass = true
    failureString = ''

    if params[:name].nil? or params[:name].empty?
      failureString += 'Zero length name. '
      didPass = false
    end

    if params[:descriptionMarkdown].nil? or params[:descriptionMarkdown].empty?
      failureString += 'Zero length Description Markdown. '
      didPass = false
    end

    if params[:jarPath].nil? or params[:jarPath].empty?
      failureString += 'Zero length Jar Path. '
      didPass = false
    end

    if didPass
      assignmentDescription = AssignmentDescription.create(name: params[:name],
                                                           markdown: params[:descriptionMarkdown],
                                                           jarPath: params[:jarPath])

      flash[:notice] = "Assignment #{assignmentDescription.name} Created Successfully."
      redirect_to assignments_list_path
    end

    if not didPass
      flash[:error] = failureString
      redirect_to assignments_new_path
    end

  end

  def createLevel
    return if sessionAdminCheckFailed
    didPass = true
    failureString = ''

    if params[:name].nil? or params[:name].empty?
      failureString += 'Zero length name. '
      didPass = false
    end

    if params[:descriptionMarkdown].nil? or params[:descriptionMarkdown].empty?
      failureString += 'Zero length Description Markdown. '
      didPass = false
    end

    if params[:possiblePoints].nil? or params[:possiblePoints].empty?
      failureString += 'Zero length Possible Points. '
      didPass = false
    end

    if params[:assignment].nil? or params[:assignment].empty?
      failureString += 'Failed to find the assignment. '
      didPass = false
    end

    if didPass
      assignmentDescription = AssignmentDescription.get(params[:assignment])

      if assignmentDescription.nil?
        failureString += "Failed to find the assignment named #{params[:assignment]}. "
        didPass = false
      else
        level = LevelDescription.create("#{assignmentDescription.key}#{params[:name]}")
        level.descriptionMarkdown = params[:descriptionMarkdown]
        level.possiblePoints = params[:possiblePoints]
        assignmentDescription.addLevel(level.key)

        flash[:notice] = "Level #{level.key} Created Successfully."
        redirect_to assignment_path(params[:assignment])
      end
    end

    if not didPass
      flash[:error] = failureString
      redirect_to assignment_path(params[:assignment])
    end

  end

end
