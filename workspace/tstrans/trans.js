// ==UserScript==
// @name         tos search
// @namespace    http://develop2.redmine.dev.smartvalue.ad.jp/redmine/issues/7307
// @version      0.1
// @description  tos search
// @author       nana4
// @match        http://*/*
// @grant        none
// ==/UserScript==

window.callback = {};

$(function() {
    var basePath = 'http://localhost/trans';
    var fromLanguage = 'ja';
    var toLanguage = 'en';

    var formatText = function(text) {
        return text.replace(/^[　 \t]+/, '').replace(/[　 \t]+$/, '').replace(/[\u30a1-\u30f6]/g, function(match) {
            return String.fromCharCode(match.charCodeAt(0) - 0x60);
        }).replace(/[！-～]/g, function(match) {
            return String.fromCharCode(match.charCodeAt(0) - 0xfee0);
        }).toLowerCase();
    };

    var load = function($selector, type) {
        callback.item = function(properties) {
            var sources = [];

            properties.forEach(function(prop) {
                if (prop.text[fromLanguage] === null || prop.text[toLanguage] === null) return;

                sources.push({
                    label: prop.text[fromLanguage] + '(' + prop.text[toLanguage] + ')',
                    value: prop.text[toLanguage],
                    search: formatText(prop.text[fromLanguage] + "\t" + prop.text[toLanguage]),
                });
            });

            $selector.autocomplete({
                source: function(req, res) {
                    var text = formatText(req.term);
                    var ressources = [];
                    var count = 0;
                    sources.some(function(source) {
                        if (source.search.includes(text)) {
                            ressources.push(source);
                            count++;
                        }
                        return count >= 20;
                    });
                    return res(ressources);
                }
            });
        };

        $.getScript(basePath + '/' + type + '.json');
    };

    load($('#q'), 'item');
});