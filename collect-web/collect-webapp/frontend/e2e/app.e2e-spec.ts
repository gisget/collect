import { CollectSurveyVisualizerPage } from './app.po';

describe('collect-survey-visualizer App', () => {
  let page: CollectSurveyVisualizerPage;

  beforeEach(() => {
    page = new CollectSurveyVisualizerPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
