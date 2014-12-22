class CreateLevelDescriptions < ActiveRecord::Migration
  def change
    create_table :level_descriptions do |t|
      t.references :assignment_description, index: true

      t.string  :name
      t.integer :levelNumber
      t.integer :points
      t.integer :extra_credit
      t.string  :markdown

      t.timestamps
    end
  end
end
