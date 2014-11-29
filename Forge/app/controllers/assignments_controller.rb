class AssignmentsController < ApplicationController

  include SessionHelper

  def list
    return if sessionActiveCheckFailed
  end

  def show
    return if sessionActiveCheckFailed

    @assignment = AssignmentDescription.get(params[:name])
    if @assignment.nil?
      flash[:error] = "Cannot find an assignment by the name: #{params[:name]}"
      redirect_to assignments_list_path
    end
  end

  def destroy
    return if sessionAdminCheckFailed

    @assignment = AssignmentDescription.get(params[:name])
    if @assignment.nil?
      flash[:error] = "Cannot find an assignment by the name: #{params[:name]}"
    else
      @assignment.delete
      flash[:notice] = "Assignment #{@assignment.key} deleted successfully."
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
      assignmentDescription = AssignmentDescription.create(params[:name])
      assignmentDescription.descriptionMarkdown = params[:descriptionMarkdown]
      assignmentDescription.jarPath = params[:jarPath]

      flash[:notice] = "Assignment #{assignmentDescription.key} Created Successfully."
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
