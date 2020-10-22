package com.puo.fireman_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puo.arcore_project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChoicesAdapter extends RecyclerView.Adapter {

  private List<Object> choicesList;
  private static RecyclerViewClickListener itemListener;

  public ChoicesAdapter(List<Object> choicesList, RecyclerViewClickListener itemListener) {
    this.choicesList = choicesList;
    this.itemListener = itemListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.quiz_choice_row, parent, false);

    return new ChoiceHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ChoiceHolder choiceHolder = (ChoiceHolder) holder;

    Object content = choicesList.get(position);

    if (content instanceof String) {
      choiceHolder.choiceImage.setVisibility(View.GONE);
      choiceHolder.choiceText.setVisibility(View.VISIBLE);
      choiceHolder.choiceText.setText((String) content);
    }
    else if (content instanceof Integer) {
      Picasso.get().load((int) content).into(choiceHolder.choiceImage);
      choiceHolder.choiceImage.setVisibility(View.VISIBLE);
      choiceHolder.choiceText.setVisibility(View.GONE);
      choiceHolder.choiceText.setText("");
    }
  }

  public interface RecyclerViewClickListener {
    public void answerClicked(View v, int position);
  }

  @Override
  public int getItemCount() {
    return choicesList.size();
  }

  public class ChoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView choiceText;
    public ImageView choiceImage;

    public ChoiceHolder(View view) {
      super(view);
      choiceText = view.findViewById(R.id.choiceText);
      choiceImage = view.findViewById(R.id.choiceImage);
      view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      itemListener.answerClicked(view, this.getLayoutPosition());
    }
  }
}
