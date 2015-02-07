class FlameController < ApplicationController
  def show
    return if sessionActiveCheckFailed

    # respond_to do |format|
    #   format.json { render json: { success: true, html: [render_to_string('flame/getting_starteding_started.html.erb', layout: false)] } }
    #   format.html { render :nothing => true }
    # end

  end
end
